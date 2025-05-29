package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface EmojiServiceV1 {

  EmojiResponseDto createOne(
      String name, MultipartFile multipartFile, User admin);
}
