package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.products.entity.Product;
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
      List<MultipartFile> productImageList, String nickname, UUID productId);

  void deleteS3Object(String url);

  List<Image> toEntityProductListAndSaveAll(
      List<S3ImageResponseDto> uploadedImageList, User loginUser, Product product);

  List<Image> uploadEmojiImage(
      List<MultipartFile> multipartFileList, User seller, UUID productId);

  String changeUserPath(
      String beforeNickname, String afterNickname, List<UUID> productIdList);

  void deleteProfileImage(String ownerNickname);

  String reloadImageAndChangeUserPath(
      User user, MultipartFile profile, String beforeNickname,
      String afterNickname, List<UUID> productIdList);

  Image changeEmojiImage(
      MultipartFile emojiImage, Emoji emoji, User emojiOwner);

  void deleteEmojiImage(Image deleteImage);
}
