package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Extension;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.awsS3.service.S3Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
    Extension.extractFromFilename(originalFilename);

    S3ResponseDto s3ResponseDto = null;
    try {
      // 1. S3 업로드 (트랜잭션 비적용)
      s3ResponseDto = s3Service.uploadProfile(profileImage, user.getNickname());

      // 2. 기존 이미지가 있다면 S3에서 삭제, DB 엔티티 정보 갱신
      Image originalImage = findByUserIdGet(user.getId());
      if (originalImage != null) {
        // 이전 이미지 S3에서 삭제
        s3Service.deleteProfile(originalImage.getUrl());
        // DB 정보만 새 이미지로 업데이트
        originalImage.updateImage(
            s3ResponseDto.getUrl(),
            s3ResponseDto.getFileName()
        );
        return originalImage.getUrl();
      }

      // 3. 신규 이미지 DB 저장
      Image image = createAndSaveImage(s3ResponseDto, user);
      return image.getUrl();

    } catch (Exception e) {
      // 4. 예외 발생 시 S3에 업로드된 파일 보상(Undo) 삭제
      if (s3ResponseDto != null) {
        try {
          s3Service.deleteProfile(s3ResponseDto.getUrl());
        } catch (Exception ex) {
          log.warn("S3 보상 삭제 실패: {}", s3ResponseDto.getUrl(), ex);
        }
      }
      throw e;
    }
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
      String originalFilename = profileImage.getOriginalFilename();
      Extension.extractFromFilename(originalFilename);
      return s3Service.uploadProfile(profileImage, afterNickname).getUrl();
    }
    String newUrl = s3Service.changePath(beforeNickname, afterNickname);
    if(image != null) image.updateProfile(afterNickname, newUrl);
    return newUrl;
  }


  @Transactional
  public Image createAndSaveImage(
      S3ResponseDto s3ResponseDto, User user){
    Image image = ImageConverter.toEntity(s3ResponseDto, user);
    imageRepository.save(image);
    return image;
  }

  @Override
  @Transactional
  public List<Image> uploadAndRegisterProductImage(
      List<MultipartFile> productImageList, User user) {
    if (productImageList == null || productImageList.isEmpty()) {
      throw new FileCustomException(FileExceptionCode.FILE_IS_REQUIRED);
    }
    if (user.getNickname() == null) {
      throw new FileCustomException(FileExceptionCode.NICKNAME_REQUIRED_FOR_PROFILE_UPLOAD);
    }

    List<S3ResponseDto> uploadedImages = new ArrayList<>();
    List<Image> savedImages = new ArrayList<>();
    try {
      // 1. S3에 여러 이미지 업로드 (트랜잭션 비적용)
      uploadedImages = s3Service.uploadProductImageList(productImageList, user.getNickname());

      // 2. DB에 각 이미지 엔티티 등록/저장 (여기서 실패할 수 있음)
      for (S3ResponseDto s3Dto : uploadedImages) {
        Image image = Image.builder()
            .url(s3Dto.getUrl())
            .fileName(s3Dto.getFileName())
            .ownerNickname(user.getNickname())
            .build();
        savedImages.add(imageRepository.save(image));
      }
      // 3. 성공한 경우 DB 엔티티 리스트 반환
      return savedImages;

    } catch (Exception e) {
      // 4. 예외 발생 시 S3에 업로드된 모든 파일 보상(삭제)
      for (S3ResponseDto dto : uploadedImages) {
        try {
          s3Service.deleteProfile(dto.getUrl());
        } catch (Exception deleteEx) {
          log.warn("S3 상품 이미지 보상 삭제 실패: {}", dto.getUrl(), deleteEx);
        }
      }
      throw e; // 예외 재전파 (DB 트랜잭션 롤백)
    }
  }

  @Transactional(readOnly = true)
  public Image findByUserId(UUID userId){
    return imageRepository.findByUserId(userId).orElseThrow(
        ()-> new FileCustomException(FileExceptionCode.NOT_FOUND));
  }

  public Image findByUserIdGet(UUID userId){
    return imageRepository.findByUserId(userId).orElse(null);
  }
}
