package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceV1 {

  Image uploadProfileImage(MultipartFile profileImage, User user);
}
