package com.example.memecommerceback.domain.meme.service;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

public interface MemeServiceV1 {

  MemeResponseDto.CreateDto create(
      MemeRequestDto.CreateDto requestDto, User user);

  MemeResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      Long memeId, boolean isApproved, String notificationMessage, User admin);

  MemeResponseDto.UpdateOneDto updateOne(
      Long memeId, MemeRequestDto.UpdateOneDto requestDto, User user);

  Page<MemeResponseDto.ReadOneDto> readPageByAdmin(
      int page, int size, int year, int quarter);

  Page<MemeResponseDto.ReadSummaryOneDto> readSummaryPage(
      int page, int size, int year, int quarter);

  void deleteMany(
      List<Long> deletedMemeIdList, String deletedMessage, User admin);

  Meme findById(Long memeId);
}
