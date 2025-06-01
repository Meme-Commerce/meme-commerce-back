package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.entity.ImageType;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;
import com.example.memecommerceback.global.awsS3.service.S3Service;
import com.example.memecommerceback.global.awsS3.utils.S3Utils;
import com.example.memecommerceback.global.utils.FileUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImplV1 implements ImageServiceV1 {

  @Value("${cloud.aws.s3.url}")
  private String s3Url;

  @PersistenceContext
  private EntityManager entityManager;

  private final S3Service s3Service;
  private final ImageRepository imageRepository;

  @Override
  @Transactional
  public String uploadAndRegisterUserProfileImage(
      MultipartFile profileImage, User user) {
    if (profileImage == null || profileImage.isEmpty()) {
      throw new FileCustomException(FileExceptionCode.FILE_IS_REQUIRED);
    }
    if (user.getNickname() == null) {
      throw new FileCustomException(FileExceptionCode.NICKNAME_REQUIRED_FOR_PROFILE_UPLOAD);
    }

    String originalFilename = profileImage.getOriginalFilename();
    FileUtils.extractExtensionFromImageName(originalFilename);

    S3ImageResponseDto s3ImageResponseDto
        = s3Service.uploadProfile(profileImage, user.getNickname());

    Image originalImage
        = imageRepository.findByUserIdAndImageType(
        user.getId(), ImageType.PROFILE).orElse(null);

    if (originalImage != null) {
      String s3ProfileUrl =
          s3Url + originalImage.getPrefixUrl() + originalImage.getFileName();
      s3Service.deleteS3Object(s3ProfileUrl);
      originalImage.updateImage(
          s3ImageResponseDto.getPrefixUrl(), s3ImageResponseDto.getFileName());
      return originalImage.getUrl();
    }

    Image image = createAndSaveImage(s3ImageResponseDto, user, ImageType.PROFILE);
    return image.getUrl();
  }

  @Override
  @Transactional
  public void deleteProductImageList(UUID productId, UUID userId) {
    List<Image> imageList = imageRepository.findAllByProductId(productId);
    try {
      imageRepository.deleteAllById(imageList.stream().map(Image::getId).toList());
      for (Image image : imageList) {
        s3Service.deleteS3Object(getS3FullUrl(image));
      }
    } catch (Exception e) {
      log.error("상품 이미지 삭제 실패 (productId: {}, userId: {})", productId, userId, e);
      throw new FileCustomException(FileExceptionCode.FAILED_DELETE_IMAGE_S3);
    }
  }

  @Override
  @Transactional
  public String changeUserPath(
      String beforeNickname, String afterNickname, List<UUID> productIdList) {
    // 이모지 타입은 가지고 오지 않는 이유는 Emoji타입의 주인은 admin이 등록하기 때문
    // s3 경로도 Emoji는 해당 어드민의 닉네임으로 보관하지 않기에,
    if(productIdList == null || productIdList.isEmpty()){
      s3Service.changeProfilePath(beforeNickname, afterNickname);
    }else{
      for(UUID productId : productIdList){
        s3Service.changePath(beforeNickname, afterNickname, productId);
      }
    }

    Image image
        = imageRepository.findByOwnerNicknameAndImageType(
        beforeNickname, ImageType.PROFILE).orElseThrow(
        () -> new FileCustomException(FileExceptionCode.NOT_FOUND));
    image.updateOwnerNicknameAndPrefix(
        afterNickname, S3Utils.getS3UserProfilePrefix(afterNickname));

    int updatedProductImageRow = imageRepository.updateImageOwnerNicknameAndPrefixByType(
        beforeNickname, afterNickname, ImageType.PRODUCT,
        S3Utils.USER_PREFIX + afterNickname + S3Utils.PRODUCT_PREFIX);
    log.info("총 {}건의 상품 이미지 row가 업데이트되었습니다.", updatedProductImageRow);

    String imageUrl = s3Url + image.getUrl();
    entityManager.clear();
    return imageUrl;
  }

  @Transactional
  public Image createAndSaveImage(
      S3ImageResponseDto s3ResponseDto, User user, ImageType imageType) {
    Image image = ImageConverter.toEntity(s3ResponseDto, user, imageType);
    imageRepository.save(image);
    return image;
  }

  @Override
  @Transactional
  public List<S3ImageResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname, UUID productId) {
    return s3Service.uploadProductImageList(productImageList, nickname, productId);
  }

  @Override
  @Transactional
  public List<Image> uploadEmojiImage(
      List<MultipartFile> multipartFileList, User seller, UUID productId) {
    if (multipartFileList == null || multipartFileList.isEmpty()) {
      throw new FileCustomException(FileExceptionCode.EMPTY_FILE);
    }
    List<S3ImageResponseDto> s3ImageResponseDtoList
        = s3Service.uploadEmojiImageList(
        multipartFileList, seller.getNickname(), productId);
    return s3ImageResponseDtoList.stream().map(
        s3ImageResponseDto -> {
          return createAndSaveImage(s3ImageResponseDto, seller, ImageType.EMOJI);
        }
    ).toList();
  }

  @Override
  @Transactional
  public List<Image> toEntityProductListAndSaveAll(
      List<S3ImageResponseDto> uploadedImageList, User loginUser, Product product) {
    List<Image> imageList
        = ImageConverter.toEntityList(
        uploadedImageList, loginUser, ImageType.PRODUCT, product);
    return imageRepository.saveAll(imageList);
  }

  @Override
  @Transactional
  public void deleteS3Object(String url) {
    s3Service.deleteS3Object(url);
  }

  @Override
  @Transactional
  public void deleteProfileImage(String ownerNickname) {
    Image image
        = imageRepository.findByOwnerNicknameAndImageType(ownerNickname, ImageType.PROFILE)
        .orElseThrow(() -> new FileCustomException(FileExceptionCode.NOT_FOUND));
    s3Service.deleteS3Object(getS3FullUrl(image));
    imageRepository.deleteById(image.getId());
  }

  @Override
  @Transactional
  public String reloadImageAndChangeUserPath(
      User user, MultipartFile profile, String beforeNickname,
      String afterNickname, List<UUID> productIdList) {
    if (user.getProfileImage() == null) {
      uploadAndRegisterUserProfileImage(profile, user);
      return changeUserPath(beforeNickname, afterNickname, productIdList);
    }
    deleteS3Object(user.getProfileImage());
    uploadAndRegisterUserProfileImage(profile, user);
    return changeUserPath(beforeNickname, afterNickname, productIdList);
  }

  @Override
  @Transactional
  public Image changeEmojiImage(
      MultipartFile emojiImage, Emoji emoji, User emojiOwner) {
    Image image = emoji.getImage();
    deleteS3Object(getS3FullUrl(image));

    List<S3ImageResponseDto> s3ImageResponseDtoList
        = s3Service.uploadEmojiImageList(
        List.of(emojiImage), emojiOwner.getNickname(), emoji.getProduct().getId());

    S3ImageResponseDto s3ImageResponseDto = s3ImageResponseDtoList.get(0);
    image.updateEmojiImage(
        s3ImageResponseDto.getPrefixUrl(), s3ImageResponseDto.getFileName(),
        s3ImageResponseDto.getOriginalName(), s3ImageResponseDto.getExtension());

    return image;
  }

  @Override
  @Transactional
  public void deleteEmojiImage(Image deleteImage){
    s3Service.deleteS3Object(getS3FullUrl(deleteImage));
    imageRepository.deleteById(deleteImage.getId());
  }

  private String getS3FullUrl(Image image){
    return s3Url + image.getUrl();
  }
}