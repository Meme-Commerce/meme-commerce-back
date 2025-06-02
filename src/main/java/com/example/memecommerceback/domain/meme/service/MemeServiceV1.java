package com.example.memecommerceback.domain.meme.service;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.users.entity.User;

public interface MemeServiceV1 {

  MemeResponseDto.CreateDto create(
      MemeRequestDto.CreateDto requestDto, User user);

  MemeResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      Long memeId, boolean isApproved, String notificationMessage, User admin);
}
