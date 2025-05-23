package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.users.entity.User;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceV1 {

  String uploadAndRegisterUserProfileImage(
      MultipartFile profileImage, User user);

  void deleteProfile(UUID userId);

  String changeProfilePath(String beforeNickname, String afterNickname);
}
