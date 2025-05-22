package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImplV1 implements ImageServiceV1 {

  private final ImageRepository imageRepository;

  @Override
  @Transactional
  public Image uploadProfileImage(MultipartFile profileImage, User user) {
    return null;
  }
}
