package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.awsS3.service.S3Service;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImplV1 implements ImageServiceV1 {

  private final S3Service s3Service;
  private final ImageRepository imageRepository;

  @Override
  @Transactional
  public String uploadAndRegisterUserProfileImage(MultipartFile profileImage, User user) {
    if(profileImage == null || profileImage.isEmpty()){
      throw new FileCustomException(FileExceptionCode.FILE_IS_REQUIRED);
    }

    if(user.getNickname() == null){
      throw new FileCustomException(
          FileExceptionCode.NICKNAME_REQUIRED_FOR_PROFILE_UPLOAD);
    }

    S3ResponseDto s3ResponseDto
        = s3Service.uploadProfile(profileImage, user.getNickname());
    Image originalImage = findByUserIdGet(user.getId());
    if(originalImage != null){
      return originalImage.getUrl();
    }
    Image image = createAndSaveImage(s3ResponseDto, user);
    return image.getUrl();
  }

  @Override
  @Transactional
  public void deleteProfile(UUID userId) {
    Image image = findByUserId(userId);
    s3Service.deleteProfile(image.getUrl());
    imageRepository.deleteById(image.getId());
  }

  @Override
  @Transactional
  public String changeProfilePath(MultipartFile profileImage, String beforeNickname, String afterNickname) {
    Image image = imageRepository.findByOwnerNickname(beforeNickname).orElse(null);
    if(image == null && profileImage != null && !profileImage.isEmpty()){
      return s3Service.uploadProfile(profileImage, afterNickname).getUrl();
    }
    String newUrl = s3Service.changePath(beforeNickname, afterNickname);
    if(image != null) image.updateProfile(afterNickname, newUrl);
    return newUrl;
  }

  public Image createAndSaveImage(
      S3ResponseDto s3ResponseDto, User user){
    Image image = ImageConverter.toEntity(s3ResponseDto, user);
    imageRepository.save(image);
    return image;
  }

  public Image findByUserId(UUID userId){
    return imageRepository.findByUserId(userId).orElseThrow(
        ()-> new FileCustomException(FileExceptionCode.NOT_FOUND));
  }

  public Image findByUserIdGet(UUID userId){
    return imageRepository.findByUserId(userId).orElse(null);
  }
}
