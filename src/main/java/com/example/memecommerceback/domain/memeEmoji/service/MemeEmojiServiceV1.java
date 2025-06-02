package com.example.memecommerceback.domain.memeEmoji.service;

import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.users.entity.User;

public interface MemeEmojiServiceV1 {

  MemeEmojiResponseDto.CreateOneDto createOne(
      Long memeId, Long emojiId, String message, User loginUser);

  MemeEmojiResponseDto.UpdateOneDto updateOneStatusByAdmin(
      Long memeEmojiId, boolean isApproved, String reason, User loginUser);
}
