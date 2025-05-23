package com.example.memecommerceback.global.awsS3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.memecommerceback.domain.images.entity.Extension;
import com.example.memecommerceback.global.awsS3.converter.S3Converter;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.exception.AWSCustomException;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {
  private final AmazonS3 amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public S3ResponseDto uploadProfile(
      MultipartFile profileImage, String nickName){
    try {
      String originalName = profileImage.getOriginalFilename();
      Extension ext = Extension.extractFromFilename(originalName);

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(profileImage.getContentType());
      metadata.setContentLength(profileImage.getSize());

      String fileName = createUUIDFile(profileImage);
      String filePath = "users/" + nickName + "/profile/" + fileName;

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

  public void deleteProfile(String imageUrl){
    try {
      // URL 디코딩
      String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8);
      // "_" 다음 문자부터 시작하여 파일 이름 추출
      String bucketUrlPart = bucket + ".s3.";
      int pathIndex = decodedUrl.indexOf(bucketUrlPart);
      if (pathIndex == -1) {
        throw new AWSCustomException(GlobalExceptionCode.NOT_MATCHED_FILE_URL);
      }
      int keyStartIndex = decodedUrl.indexOf("/", pathIndex + bucketUrlPart.length());
      String filePath = decodedUrl.substring(keyStartIndex + 1);
      // S3에서 파일 삭제
      amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
    } catch (Exception e) {
      throw new AWSCustomException(GlobalExceptionCode.NOT_MATCHED_FILE_URL);
    }
  }

  public String changePath(String beforeNickname, String afterNickname) {
    // 1. 기존 경로의 파일 리스트를 조회 (profile 폴더에 여러 파일 있을 수 있음)
    String oldPrefix = "users/" + beforeNickname + "/profile/";
    String newPrefix = "users/" + afterNickname + "/profile/";

    // S3에서 해당 경로의 파일 목록을 불러온다
    var objectListing = amazonS3Client.listObjects(bucket, oldPrefix);

    if (objectListing.getObjectSummaries().isEmpty()) {
      log.warn("기존 프로필 이미지가 존재하지 않습니다: {}", oldPrefix);
      throw new AWSCustomException(GlobalExceptionCode.NOT_MATCHED_FILE_URL);
    }

    String newUrl = null;

    for (var s3Object : objectListing.getObjectSummaries()) {
      String oldKey = s3Object.getKey();
      String fileName = oldKey.substring(oldPrefix.length());
      String newKey = newPrefix + fileName;

      // 1. Copy
      amazonS3Client.copyObject(bucket, oldKey, bucket, newKey);
      // 2. Delete original
      amazonS3Client.deleteObject(bucket, oldKey);

      log.info("프로필 이미지 경로 변경: {} → {}", oldKey, newKey);

      newUrl = amazonS3Client.getUrl(bucket, newKey).toString();
    }
    if (newUrl == null) {
      throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
    }
    return newUrl;
  }

  private String createUUIDFile(MultipartFile file){
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || originalFilename.isBlank()) {
      throw new AWSCustomException(GlobalExceptionCode.BLANK_FILE);
    }

    String uuid = UUID.randomUUID().toString();
    return uuid + "_" + file.getOriginalFilename();
  }
}
