package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceV1 {

  String uploadAndRegisterUserProfileImage(
      MultipartFile profileImage, User user);

  void deleteProfile(UUID userId);

  String changeProfilePath(
      MultipartFile profileImage, String beforeNickname, String afterNickname);

  List<Image> uploadAndRegisterProductImage(
      List<MultipartFile> profileImageList, User user);

  void deleteProductImageList(UUID productId, UUID userId);
}
