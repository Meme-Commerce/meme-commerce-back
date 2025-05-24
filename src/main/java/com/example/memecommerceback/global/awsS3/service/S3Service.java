package com.example.memecommerceback.global.awsS3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.memecommerceback.domain.images.entity.Extension;
import com.example.memecommerceback.global.awsS3.converter.S3Converter;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.awsS3.utils.S3Utils;
import com.example.memecommerceback.global.exception.AWSCustomException;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
  public S3ResponseDto uploadProfile(
      MultipartFile profileImage, String nickname){
    try {
      String originalName = profileImage.getOriginalFilename();
      Extension ext = Extension.extractFromFilename(originalName);

      ObjectMetadata metadata = setObjectMetadata(profileImage);

      String fileName = createUUIDFile(profileImage);
      String filePath 
          = S3Utils.USER_PREFIX + nickname 
          + S3Utils.PROFILE_PREFIX + fileName;

      amazonS3Client.putObject(
          bucket, filePath, profileImage.getInputStream(), metadata);

      String url = amazonS3Client.getUrl(bucket, filePath).toString();

      log.info("s3 서비스에 파일을 등록했습니다. : " +url);

      return S3Converter.toS3ResponseDto(
          originalName, ext, fileName, url, profileImage.getSize());

    } catch (SdkClientException e) {
      log.error("AWS SDK exception occurred during file upload: {}", e.getMessage(), e);
    } catch (IOException e) {
      log.error("IO exception occurred during file upload: {}", e.getMessage(), e);
    }
    throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
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
  public String changePath(String beforenickname, String afternickname) {
    String oldPrefix = S3Utils.USER_PREFIX + beforenickname + S3Utils.PROFILE_PREFIX;
    String newPrefix = S3Utils.USER_PREFIX + afternickname + S3Utils.PROFILE_PREFIX;

    // 1. afternickname 경로가 이미 존재하면 에러 (다른 사용자가 이미 사용 중)
    var afterListing = amazonS3Client.listObjects(bucket, newPrefix);
    if (!afterListing.getObjectSummaries().isEmpty()) {
      log.warn("afterNickname 경로에 이미 파일이 존재합니다: {}", newPrefix);
      throw new AWSCustomException(GlobalExceptionCode.ALREADY_EXISTS_FILE_URL);
    }

    // 2. beforeNickname 경로 확인
    var beforeListing = amazonS3Client.listObjects(bucket, oldPrefix);
    if (!beforeListing.getObjectSummaries().isEmpty()) {
      // 2-1. before 경로의 모든 파일을 after 경로로 이동
      String newUrl = null;
      for (var s3Object : beforeListing.getObjectSummaries()) {
        String oldKey = s3Object.getKey();
        String fileName = oldKey.substring(oldPrefix.length());
        String newKey = newPrefix + fileName;

        amazonS3Client.copyObject(bucket, oldKey, bucket, newKey);
        amazonS3Client.deleteObject(bucket, oldKey);

        log.info("프로필 이미지 경로 변경: {} → {}", oldKey, newKey);
        newUrl = amazonS3Client.getUrl(bucket, newKey).toString();
      }
      if (newUrl == null) {
        throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
      }
      return newUrl;
    }

    log.warn("beforeNickname, afterNickname 모두 해당 경로에 이미지가 존재하지 않습니다.");
    // 필요하다면 기본값 or null or 예외 처리 (업로드 분기에서 저장)
    return null;
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<S3ResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname){
    List<S3ResponseDto> s3ResponseDtoList = new ArrayList<>();
    for (MultipartFile productImage : productImageList) {
      try {
        String originalName = productImage.getOriginalFilename();
        Extension ext = Extension.extractFromFilename(originalName);

        String fileName = createUUIDFile(productImage);
        String filePath
            = S3Utils.USER_PREFIX + nickname
            + S3Utils.PRODUCT_PREFIX + fileName;

        ObjectMetadata metadata = setObjectMetadata(productImage);

        amazonS3Client.putObject(
            bucket, filePath, productImage.getInputStream(), metadata);

        String url = amazonS3Client.getUrl(bucket, filePath).toString();

        log.info("s3 서비스에 파일을 등록했습니다. : " +url);

        s3ResponseDtoList.add(S3Converter.toS3ResponseDto(
            originalName, ext, fileName, url, productImage.getSize()));

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

  private String createUUIDFile(MultipartFile file){
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || originalFilename.isBlank()) {
      throw new AWSCustomException(GlobalExceptionCode.BLANK_FILE);
    }

    String uuid = UUID.randomUUID().toString();
    return uuid + "_" + file.getOriginalFilename();
  }

  private ObjectMetadata setObjectMetadata(MultipartFile multipartFile){
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(multipartFile.getContentType());
    metadata.setContentLength(multipartFile.getSize());
    return metadata;
  }
}
