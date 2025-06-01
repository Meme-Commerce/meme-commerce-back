package com.example.memecommerceback.global.awsS3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.memecommerceback.domain.files.entity.FileExtension;
import com.example.memecommerceback.domain.images.entity.ImageExtension;
import com.example.memecommerceback.domain.users.entity.User;
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
  public S3ImageResponseDto uploadProfile(
      MultipartFile profileImage, String nickname) {
    try {
      String originalName = profileImage.getOriginalFilename();
      ImageExtension ext = FileUtils.extractExtensionFromImageName(originalName);

      ObjectMetadata metadata = setObjectMetadata(profileImage);

      String prefixUrl = S3Utils.getS3UserProfilePrefix(nickname);
      String fileName = createUUIDFile(profileImage);
      String filePath = prefixUrl + fileName;
      amazonS3Client.putObject(
          bucket, filePath, profileImage.getInputStream(), metadata);

      String url = amazonS3Client.getUrl(bucket, filePath).toString();

      log.info("s3 서비스에 파일을 등록했습니다. : " + url);

      return S3Converter.toS3ImageResponseDto(
          originalName, ext, fileName, prefixUrl, profileImage.getSize());

    } catch (SdkClientException e) {
      log.error("AWS SDK exception occurred during file upload: {}", e.getMessage(), e);
    } catch (IOException e) {
      log.error("IO exception occurred during file upload: {}", e.getMessage(), e);
    }
    throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void deleteS3Object(String imageUrl) {
    System.out.println("imageUrl : "+imageUrl);
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
      String beforeNickname, String afterNickname) {

    String oldProfilePrefix = S3Utils.getS3UserProfilePrefix(beforeNickname);
    String newProfilePrefix = S3Utils.getS3UserProfilePrefix(afterNickname);
    String oldProductPrefix = S3Utils.getS3UserProductPrefix(beforeNickname);
    String newProductPrefix = S3Utils.getS3UserProductPrefix(afterNickname);

    List<S3ImageSummaryResponseDto> resultList = new ArrayList<>();

    // 1. afternickname 경로가 이미 존재하면 에러 (다른 사용자가 이미 사용 중)
    var afterProfileListing = amazonS3Client.listObjects(bucket, newProfilePrefix);
    if (!afterProfileListing.getObjectSummaries().isEmpty()) {
      log.warn("afterNickname 경로에 이미 파일이 존재합니다: {}", newProfilePrefix);
      throw new AWSCustomException(GlobalExceptionCode.ALREADY_EXISTS_FILE_URL);
    }
    var afterProductListing = amazonS3Client.listObjects(bucket, newProductPrefix);
    if (!afterProductListing.getObjectSummaries().isEmpty()) {
      log.warn("afterNickname 경로에 이미 파일이 존재합니다: {}", newProductPrefix);
      throw new AWSCustomException(GlobalExceptionCode.ALREADY_EXISTS_FILE_URL);
    }

    // 2. profile 이미지 이동
    var beforeProfileListing = amazonS3Client.listObjects(bucket, oldProfilePrefix);
    var profileSummaries = beforeProfileListing.getObjectSummaries();
    if (!profileSummaries.isEmpty()) {
      for (var s3Object : profileSummaries) {
        String oldKey = s3Object.getKey();
        String fileName = oldKey.substring(oldProfilePrefix.length());
        String newKey = newProfilePrefix + fileName;

        amazonS3Client.copyObject(bucket, oldKey, bucket, newKey);
        amazonS3Client.deleteObject(bucket, oldKey);

        log.info("프로필 이미지 경로 변경: {} → {}", oldKey, newKey);
        resultList.add(S3Converter.toS3ImageSummaryDto(newKey, fileName));
      }
    }

    // 3. product 이미지 이동
    var beforeProductListing = amazonS3Client.listObjects(bucket, oldProductPrefix);
    var productSummaries = beforeProductListing.getObjectSummaries();
    if (!productSummaries.isEmpty()) {
      for (var s3Object : productSummaries) {
        String oldKey = s3Object.getKey();
        String fileName = oldKey.substring(oldProductPrefix.length());
        String newKey = newProductPrefix + fileName;

        amazonS3Client.copyObject(bucket, oldKey, bucket, newKey);
        amazonS3Client.deleteObject(bucket, oldKey);

        log.info("상품 이미지 경로 변경: {} → {}", oldKey, newKey);
        resultList.add(S3Converter.toS3ImageSummaryDto(newKey, fileName));
      }
    }

    if (resultList.isEmpty()) {
      log.warn("beforeNickname, afterNickname 모두 해당 경로에 이미지가 존재하지 않습니다.");
      return Collections.emptyList();
    }
    return resultList;
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3ImageResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname) {
    List<S3ImageResponseDto> s3ResponseDtoList = new ArrayList<>();
    for (MultipartFile productImage : productImageList) {
      try {
        String originalName = productImage.getOriginalFilename();
        ImageExtension ext = FileUtils.extractExtensionFromImageName(originalName);

        String prefixUrl = S3Utils.getS3UserProductPrefix(nickname);
        String fileName = createUUIDFile(productImage);
        String filePath = prefixUrl + fileName;

        uploadS3Bucket(filePath, productImage);

        s3ResponseDtoList.add(S3Converter.toS3ImageResponseDto(
            originalName, ext, fileName, prefixUrl, productImage.getSize()));

      } catch (SdkClientException e) {
        log.error("AWS SDK exception occurred during file upload: {}", e.getMessage(), e);
        throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
      } catch (IOException e) {
        log.error("IO exception occurred during file upload: {}", e.getMessage(), e);
        throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
      }
    }
    return s3ResponseDtoList;
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
        String filePath
            = S3Utils.USER_PREFIX + nickname
            + S3Utils.CERTIFICATE_PREFIX + fileName;

        uploadS3Bucket(filePath, certificateFile);

        s3ResponseDtoList.add(S3Converter.toS3FileResponseDto(
            originalName, ext, fileName,
            amazonS3Client.getUrl(bucket, filePath).toString(),
            certificateFile.getSize()));

      } catch (SdkClientException e) {
        log.error("AWS SDK exception occurred during file upload: {}", e.getMessage(), e);
        throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
      } catch (IOException e) {
        log.error("IO exception occurred during file upload: {}", e.getMessage(), e);
        throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
      }
    }
    return s3ResponseDtoList;
  }

  public List<S3ImageResponseDto> uploadEmojiImageList(
      List<MultipartFile> multipartFileList, String nickname, String emojiPackName) {
    List<S3ImageResponseDto> s3ResponseDtoList = new ArrayList<>();
    for (MultipartFile multipartFile: multipartFileList) {
      try {
        String originalName = multipartFile.getOriginalFilename();
        ImageExtension ext = FileUtils.extractExtensionFromImageName(originalName);

        ObjectMetadata metadata = setObjectMetadata(multipartFile);

        String fileName = createUUIDFile(multipartFile);
        String filePath
            = S3Utils.getS3UserEmojiPrefix(nickname, emojiPackName)
            + "/"+ fileName;

        amazonS3Client.putObject(
            bucket, filePath, multipartFile.getInputStream(), metadata);

        String url = amazonS3Client.getUrl(bucket, filePath).toString();

        log.info("s3 서비스에 파일을 등록했습니다. : " + url);

        s3ResponseDtoList.add(S3Converter.toS3ImageResponseDto(
            originalName, ext, fileName, filePath, multipartFile.getSize()));

      } catch (SdkClientException e) {
        log.error("AWS SDK exception occurred during file upload: {}", e.getMessage(), e);
      } catch (IOException e) {
        log.error("IO exception occurred during file upload: {}", e.getMessage(), e);
      }
    }
    return s3ResponseDtoList;
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

  private void uploadS3Bucket(
      String filePath, MultipartFile file) throws IOException {
    ObjectMetadata metadata = setObjectMetadata(file);

    amazonS3Client.putObject(
        bucket, filePath, file.getInputStream(), metadata);

    String url = amazonS3Client.getUrl(bucket, filePath).toString();

    log.info("s3 서비스에 파일을 등록했습니다. : " + url);
  }
}
