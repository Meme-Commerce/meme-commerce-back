package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.awsS3.service.S3Service;
import com.example.memecommerceback.global.utils.FileUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImplV1 implements ImageServiceV1 {

  private final S3Service s3Service;
  private final ImageRepository imageRepository;

  @Override
  @Transactional
  public String uploadAndRegisterUserProfileImage(MultipartFile profileImage, User user) {
    if (profileImage == null || profileImage.isEmpty()) {
      throw new FileCustomException(FileExceptionCode.FILE_IS_REQUIRED);
    }
    if (user.getNickname() == null) {
      throw new FileCustomException(FileExceptionCode.NICKNAME_REQUIRED_FOR_PROFILE_UPLOAD);
    }

    String originalFilename = profileImage.getOriginalFilename();
    FileUtils.extractFromImageName(originalFilename);

    S3ResponseDto s3ResponseDto = s3Service.uploadProfile(profileImage, user.getNickname());

    Image originalImage = findByUserIdGet(user.getId());
    if (originalImage != null) {
      s3Service.deleteS3Object(originalImage.getUrl());
      originalImage.updateImage(s3ResponseDto.getUrl(), s3ResponseDto.getFileName());
      return originalImage.getUrl();
    }

    Image image = createAndSaveImage(s3ResponseDto, user);
    return image.getUrl();
  }

  @Override
  @Transactional
  public List<Image> uploadAndRegisterProductImage(
      List<MultipartFile> productImageList, User user) {

    // 검증 로직
    if (productImageList == null || productImageList.isEmpty()) {
      throw new FileCustomException(FileExceptionCode.FILE_IS_REQUIRED);
    }
    if (productImageList.size() > 5) {
      throw new FileCustomException(FileExceptionCode.NOT_REGISTER_OVER_MAX_PRODUCT_IMAGES);
    }
    if (user.getNickname() == null) {
      throw new FileCustomException(FileExceptionCode.NICKNAME_REQUIRED_FOR_PROFILE_UPLOAD);
    }

    // 확장자 검증
    for (MultipartFile productImage : productImageList) {
      if (productImage == null || productImage.isEmpty()) {
        throw new FileCustomException(FileExceptionCode.FILE_IS_REQUIRED);
      }
      String originalFilename = productImage.getOriginalFilename();
      FileUtils.extractFromImageName(originalFilename);
    }

    List<S3ResponseDto> uploadedImages = s3Service.uploadProductImageList(productImageList, user.getNickname());
    List<Image> imageList = ImageConverter.toEntityList(uploadedImages, user);
    return imageRepository.saveAll(imageList);
  }

  @Override
  @Transactional
  public void deleteProductImageList(UUID productId, UUID userId) {
    List<Image> imageList = imageRepository.findAllByProductId(productId);
    try {
      imageRepository.deleteAllById(imageList.stream().map(Image::getId).toList());
      for (Image image : imageList) {
        s3Service.deleteS3Object(image.getUrl());
      }
    } catch (Exception e) {
      log.error("상품 이미지 삭제 실패 (productId: {}, userId: {})", productId, userId, e);
      throw new FileCustomException(FileExceptionCode.FAILED_DELETE_IMAGE_S3);
    }
  }

  @Override
  @Transactional
  public String changeProfilePath(MultipartFile profileImage, String beforeNickname, String afterNickname) {
    Image image = imageRepository.findByOwnerNickname(beforeNickname).orElse(null);
    if(image == null && profileImage != null && !profileImage.isEmpty()){
      String originalFilename = profileImage.getOriginalFilename();
      FileUtils.extractFromImageName(originalFilename);
      return s3Service.uploadProfile(profileImage, afterNickname).getUrl();
    }
    String newUrl = s3Service.changePath(beforeNickname, afterNickname);
    if(image != null) image.updateProfile(afterNickname, newUrl);
    return newUrl;
  }

  @Transactional
  public Image createAndSaveImage(S3ResponseDto s3ResponseDto, User user) {
    Image image = ImageConverter.toEntity(s3ResponseDto, user);
    imageRepository.save(image);
    return image;
  }

  public Image findByUserIdGet(UUID userId) {
    return imageRepository.findByUserId(userId).orElse(null);
  }

  @Override
  @Transactional
  public List<S3ResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname){
    return s3Service.uploadProductImageList(productImageList, nickname);
  }

  @Override
  @Transactional
  public List<Image> toEntityListAndSaveAll(
      List<S3ResponseDto> uploadedImageList, User loginUser) {
    List<Image> imageList
        = ImageConverter.toEntityList(uploadedImageList, loginUser);
    return imageRepository.saveAll(imageList);
  }

  @Override
  @Transactional
  public void deleteS3Object(String url){
    s3Service.deleteS3Object(url);
  }
}
