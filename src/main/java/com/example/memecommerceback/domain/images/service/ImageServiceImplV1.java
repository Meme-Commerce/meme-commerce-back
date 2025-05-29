package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.entity.ImageType;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;
import com.example.memecommerceback.global.awsS3.dto.S3ImageSummaryResponseDto;
import com.example.memecommerceback.global.awsS3.service.S3Service;
import com.example.memecommerceback.global.awsS3.utils.S3Utils;
import com.example.memecommerceback.global.utils.FileUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

  @PersistenceContext
  private EntityManager entityManager;

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

    S3ImageResponseDto s3ImageResponseDto
        = s3Service.uploadProfile(profileImage, user.getNickname());

    Image originalImage = findByUserIdGet(user.getId());
    if (originalImage != null) {
      s3Service.deleteS3Object(originalImage.getPrefixUrl() + originalImage.getFileName());
      originalImage.updateImage(
          s3ImageResponseDto.getPrefixUrl(), s3ImageResponseDto.getFileName());
      return originalImage.getUrl();
    }

    Image image = createAndSaveImage(s3ImageResponseDto, user, ImageType.PROFILE);
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

    List<S3ImageResponseDto> uploadedImages = s3Service.uploadProductImageList(productImageList,
        user.getNickname());
    List<Image> imageList = ImageConverter.toEntityList(uploadedImages, user, ImageType.PRODUCT);
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
  public void changeUserPath(String beforeNickname, String afterNickname) {
    // 이모지 타입은 가지고 오지 않는 이유는 Emoji타입의 주인은 admin이 등록하기 때문
    // s3 경로도 Emoji는 해당 어드민의 닉네임으로 보관하지 않기에,
    s3Service.changePath(beforeNickname, afterNickname);

    int updatedProfileImageRow = imageRepository.updateImageOwnerNicknameAndPrefixByType(
        beforeNickname, afterNickname, ImageType.PROFILE,
        S3Utils.getS3UserProfilePrefix(afterNickname));
    if(updatedProfileImageRow > 1){
      throw new FileCustomException(FileExceptionCode.DUPLICATE_PROFILE_IMAGE);
    }
    log.info("총 {}건의 프로필 이미지 row가 업데이트되었습니다.", updatedProfileImageRow);

    int updatedProductImageRow = imageRepository.updateImageOwnerNicknameAndPrefixByType(
        beforeNickname, afterNickname, ImageType.PRODUCT,
        S3Utils.USER_PREFIX + afterNickname + S3Utils.PRODUCT_PREFIX);
    log.info("총 {}건의 상품 이미지 row가 업데이트되었습니다.", updatedProductImageRow);

    entityManager.clear();
  }

  @Override
  @Transactional
  public String changeProfilePath(MultipartFile profileImage, String beforeNickname,
      String afterNickname) {
    Image image = imageRepository.findByOwnerNickname(beforeNickname).orElse(null);
    if (image == null && profileImage != null && !profileImage.isEmpty()) {
      String originalFilename = profileImage.getOriginalFilename();
      FileUtils.extractFromImageName(originalFilename);
      S3ImageResponseDto s3ImageResponseDto
          = s3Service.uploadProfile(profileImage, afterNickname);
      return s3ImageResponseDto.getPrefixUrl() + s3ImageResponseDto.getFileName();
    }
    String newUrl = s3Service.changeProfilePath(beforeNickname, afterNickname);
    if (image != null) {
      image.updateProfile(
          afterNickname, image.getPrefixUrl(), image.getFileName());
    }
    return newUrl;
  }

  @Transactional
  public Image createAndSaveImage(
      S3ImageResponseDto s3ResponseDto, User user, ImageType imageType) {
    Image image = ImageConverter.toEntity(s3ResponseDto, user, imageType);
    imageRepository.save(image);
    return image;
  }

  public Image findByUserIdGet(UUID userId) {
    return imageRepository.findByUserId(userId).orElse(null);
  }

  @Override
  @Transactional
  public List<S3ImageResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname) {
    return s3Service.uploadProductImageList(productImageList, nickname);
  }

  @Override
  @Transactional
  public Image uploadEmojiImage(MultipartFile multipartFile, User admin) {
    if(multipartFile == null || multipartFile.isEmpty()){
      throw new FileCustomException(FileExceptionCode.EMPTY_FILE);
    }
    S3ImageResponseDto s3ImageResponseDto
        = s3Service.uploadEmojiImage(multipartFile);
    return createAndSaveImage(s3ImageResponseDto, admin, ImageType.EMOJI);
  }

  @Override
  @Transactional
  public List<Image> toEntityProductListAndSaveAll(
      List<S3ImageResponseDto> uploadedImageList, User loginUser) {
    List<Image> imageList
        = ImageConverter.toEntityList(
            uploadedImageList, loginUser, ImageType.PRODUCT);
    return imageRepository.saveAll(imageList);
  }

  @Override
  @Transactional
  public void deleteS3Object(String url) {
    s3Service.deleteS3Object(url);
  }
}
