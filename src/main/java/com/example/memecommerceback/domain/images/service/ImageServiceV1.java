package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceV1 {

  String uploadAndRegisterUserProfileImage(
      MultipartFile profileImage, User user);

  void deleteProductImageList(UUID productId, UUID userId);

  List<S3ImageResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname);

  void deleteS3Object(String url);

  List<Image> toEntityProductListAndSaveAll(
      List<S3ImageResponseDto> uploadedImageList, User loginUser);

  List<Image> uploadAndRegisterProductImage(
      List<MultipartFile> productImageList, User user);

  Image uploadEmojiImage(MultipartFile multipartFile, User admin);

  String changeUserPath(String beforeNickname, String afterNickname);

  void deleteProfileImage(String ownerNickname);

  String reloadImageAndChangeUserPath(
      User user, MultipartFile profile, String beforeNickname, String afterNickname);
}
