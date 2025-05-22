package com.example.memecommerceback.global.awsS3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.memecommerceback.domain.images.entity.Extension;
import com.example.memecommerceback.global.awsS3.converter.S3Converter;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.AWSCustomException;
import java.io.IOException;
import java.util.Objects;
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
  private final AmazonS3Client amazonS3Client;

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
      String filePath = nickName + "/profile/" + fileName;

      amazonS3Client.putObject(
          bucket, filePath, profileImage.getInputStream(), metadata);

      String url = amazonS3Client.getUrl(bucket, filePath).toString();

      log.info("s3 서비스에 파일을 등록했습니다. : " +url);

      return S3Converter.toS3ResponseDto(
          originalName, ext, fileName, url, profileImage.getSize());

    } catch (SdkClientException | IOException e) {
      throw new AWSCustomException(GlobalExceptionCode.UPLOAD_FAIL);
    }
  }

  private String createUUIDFile(MultipartFile file){
    if (Objects.requireNonNull(file.getOriginalFilename()).isBlank()) {
      throw new AWSCustomException(GlobalExceptionCode.BLANK_FILE);
    }

    String uuid = UUID.randomUUID().toString();
    return uuid + "_" + file.getOriginalFilename();
  }
}
