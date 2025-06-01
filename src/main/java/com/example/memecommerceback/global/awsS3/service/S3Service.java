package com.example.memecommerceback.global.awsS3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.memecommerceback.domain.files.entity.FileExtension;
import com.example.memecommerceback.domain.images.entity.ImageExtension;
import com.example.memecommerceback.global.awsS3.converter.S3Converter;
import com.example.memecommerceback.global.awsS3.dto.S3FileResponseDto;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;
import com.example.memecommerceback.global.awsS3.dto.S3ImageSummaryResponseDto;
import com.example.memecommerceback.global.awsS3.utils.S3Utils;
import com.example.memecommerceback.global.exception.AWSCustomException;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.utils.FileUtils;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public S3ImageResponseDto uploadProfile(MultipartFile profileImage, String nickname) {
    String prefixUrl = S3Utils.getS3UserProfilePrefix(nickname);
    return uploadSingleImage(profileImage, prefixUrl);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void deleteS3Object(String imageUrl) {
    try {
      String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8);
      String bucketUrlPart = bucket + ".s3.";
      int pathIndex = decodedUrl.indexOf(bucketUrlPart);
      if (pathIndex == -1) {
        throw new AWSCustomException(GlobalExceptionCode.NOT_MATCHED_FILE_URL);
      }
      int keyStartIndex = decodedUrl.indexOf("/", pathIndex + bucketUrlPart.length());
      String filePath = decodedUrl.substring(keyStartIndex + 1);
      amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
      log.info("S3 이미지 삭제 완료: {}", filePath);
    } catch (Exception e) {
      log.error("S3 이미지 삭제 실패: {}", imageUrl, e);
      throw new AWSCustomException(GlobalExceptionCode.NOT_MATCHED_FILE_URL);
    }
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3ImageSummaryResponseDto> changePath(
      String beforeNickname, String afterNickname, UUID productId) {

    String oldProfilePrefix = S3Utils.getS3UserProfilePrefix(beforeNickname);
    String newProfilePrefix = S3Utils.getS3UserProfilePrefix(afterNickname);
    String oldProductPrefix = S3Utils.getS3UserProductPrefix(beforeNickname, productId);
    String newProductPrefix = S3Utils.getS3UserProductPrefix(afterNickname, productId);

    // 경로 존재 확인
    validatePathNotExists(newProfilePrefix, "프로필");
    validatePathNotExists(newProductPrefix, "상품");

    List<S3ImageSummaryResponseDto> resultList = new ArrayList<>();
    resultList.addAll(moveFiles(oldProfilePrefix, newProfilePrefix, "프로필 이미지 경로 변경"));
    resultList.addAll(moveFiles(oldProductPrefix, newProductPrefix, "상품 이미지 경로 변경"));

    if (resultList.isEmpty()) {
      log.warn("beforeNickname, afterNickname 모두 해당 경로에 이미지가 존재하지 않습니다.");
      return Collections.emptyList();
    }
    return resultList;
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3ImageSummaryResponseDto> changeProfilePath(
      String beforeNickname, String afterNickname) {

    String oldProfilePrefix = S3Utils.getS3UserProfilePrefix(beforeNickname);
    String newProfilePrefix = S3Utils.getS3UserProfilePrefix(afterNickname);

    // 경로 존재 확인
    validatePathNotExists(newProfilePrefix, "프로필");

    List<S3ImageSummaryResponseDto> resultList =
        moveFiles(oldProfilePrefix, newProfilePrefix, "프로필 이미지 경로 변경");

    if (resultList.isEmpty()) {
      log.warn("beforeNickname, afterNickname 모두 해당 경로에 이미지가 존재하지 않습니다.");
      return Collections.emptyList();
    }
    return resultList;
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3ImageResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname, UUID mainProductId) {
    String prefixUrl = S3Utils.getS3UserProductPrefix(nickname, mainProductId);
    return uploadImageList(productImageList, prefixUrl);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3FileResponseDto> uploadCertificateFileList(
      List<MultipartFile> multipartFileList, String nickname) {
    List<S3FileResponseDto> s3ResponseDtoList = new ArrayList<>();
    for (MultipartFile certificateFile : multipartFileList) {
      try {
        String originalName = certificateFile.getOriginalFilename();
        FileExtension ext = FileUtils.extractExtensionFromFilename(originalName);

        String fileName = createUUIDFile(certificateFile);
        String filePath = S3Utils.USER_PREFIX + nickname + S3Utils.CERTIFICATE_PREFIX + fileName;

        uploadS3Bucket(filePath, certificateFile);

        s3ResponseDtoList.add(S3Converter.toS3FileResponseDto(
            originalName, ext, fileName,
            amazonS3Client.getUrl(bucket, filePath).toString(),
            certificateFile.getSize()));

      } catch (SdkClientException | IOException e) {
        log.error("Certificate file upload failed: {}", e.getMessage(), e);
        throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
      }
    }
    return s3ResponseDtoList;
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3ImageResponseDto> uploadEmojiImageList(
      List<MultipartFile> multipartFileList, String nickname, UUID mainProductId) {
    String prefixUrl = S3Utils.getS3UserEmojiPrefix(nickname, mainProductId);
    return uploadImageList(multipartFileList, prefixUrl);
  }

  // ===== 공통 메서드들 =====

  private S3ImageResponseDto uploadSingleImage(MultipartFile file, String prefixUrl) {
    try {
      String originalName = file.getOriginalFilename();
      ImageExtension ext = FileUtils.extractExtensionFromImageName(originalName);
      String fileName = createUUIDFile(file);
      String filePath = prefixUrl + fileName;

      uploadS3Bucket(filePath, file);

      return S3Converter.toS3ImageResponseDto(
          originalName, ext, fileName, prefixUrl, file.getSize());

    } catch (SdkClientException | IOException e) {
      log.error("파일 업로드 실패: {}", e.getMessage(), e);
      throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
    }
  }

  private List<S3ImageResponseDto> uploadImageList(
      List<MultipartFile> imageList, String prefixUrl) {
    return imageList.stream()
        .map(file -> uploadSingleImage(file, prefixUrl))
        .collect(Collectors.toList());
  }

  private List<S3ImageSummaryResponseDto> moveFiles(
      String oldPrefix, String newPrefix, String logMessage) {
    List<S3ImageSummaryResponseDto> resultList = new ArrayList<>();

    var listing = amazonS3Client.listObjects(bucket, oldPrefix);
    var summaries = listing.getObjectSummaries();

    if (!summaries.isEmpty()) {
      for (var s3Object : summaries) {
        String oldKey = s3Object.getKey();
        String fileName = oldKey.substring(oldPrefix.length());
        String newKey = newPrefix + fileName;

        amazonS3Client.copyObject(bucket, oldKey, bucket, newKey);
        amazonS3Client.deleteObject(bucket, oldKey);

        log.info("{}: {} → {}", logMessage, oldKey, newKey);
        resultList.add(S3Converter.toS3ImageSummaryDto(newPrefix, fileName));
      }
    }

    return resultList;
  }

  private void validatePathNotExists(String prefix, String description) {
    var listing = amazonS3Client.listObjects(bucket, prefix);
    if (!listing.getObjectSummaries().isEmpty()) {
      log.warn("{} 경로에 이미 파일이 존재합니다: {}", description, prefix);
      throw new AWSCustomException(GlobalExceptionCode.ALREADY_EXISTS_FILE_URL);
    }
  }

  private String createUUIDFile(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || originalFilename.isBlank()) {
      throw new AWSCustomException(GlobalExceptionCode.BLANK_FILE);
    }

    String uuid = UUID.randomUUID().toString();
    return uuid + "_" + file.getOriginalFilename();
  }

  private ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(multipartFile.getContentType());
    metadata.setContentLength(multipartFile.getSize());
    return metadata;
  }

  private void uploadS3Bucket(String filePath, MultipartFile file) throws IOException {
    ObjectMetadata metadata = setObjectMetadata(file);

    amazonS3Client.putObject(bucket, filePath, file.getInputStream(), metadata);

    String url = amazonS3Client.getUrl(bucket, filePath).toString();
    log.info("s3 서비스에 파일을 등록했습니다. : " + url);
  }
}