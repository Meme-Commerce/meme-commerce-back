package com.example.memecommerceback.domain.memeEmoji.service;

import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

public interface MemeEmojiServiceV1 {

  MemeEmojiResponseDto.CreateOneDto createOne(
      Long memeId, Long emojiId, String name, String message, User loginUser);

  MemeEmojiResponseDto.UpdateOneDto updateOneStatusByAdmin(
      Long memeEmojiId, boolean isApproved, String reason, User loginUser);

  MemeEmojiResponseDto.UpdateOneDto updateOne(
      Long memeEmojiId, String name, String message, User loginUser);

  Page<MemeEmojiResponseDto.ReadOneDto> readPage(int page, int size);

  MemeEmojiResponseDto.ReadOneDto readOne(Long memeEmojiId, User loginUser);

  void deleteMany(
      List<Long> deletedMemeIdList, String deletedReason, User admin);

  MemeEmoji findById(Long memeEmojiId);
}
