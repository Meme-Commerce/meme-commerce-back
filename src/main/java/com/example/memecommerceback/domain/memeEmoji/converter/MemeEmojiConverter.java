package com.example.memecommerceback.domain.memeEmoji.converter;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;

public class MemeEmojiConverter {

  public static MemeEmoji toEntity(
      Meme meme, Emoji emoji, String message, String requestUserNickname){
    return MemeEmoji.builder()
        .meme(meme).emoji(emoji).message(message)
        .requestUserNickname(requestUserNickname).build();
  }

  public static MemeEmojiResponseDto.CreateOneDto toCreateOneDto(
      MemeEmoji memeEmoji, Long memeId, Long emojiId){
    return MemeEmojiResponseDto.CreateOneDto.builder()
        .memeEmojiId(memeEmoji.getId())
        .memeId(memeId).emojiId(emojiId)
        .requestMessage(memeEmoji.getMessage())
        .requestUserNickName(memeEmoji.getRequestUserNickname())
        .build();
  }

  public static MemeEmojiResponseDto.UpdateOneDto toUpdateOneDto(
      MemeEmoji memeEmoji){
    return MemeEmojiResponseDto.UpdateOneDto.builder()
        .memeEmojiId(memeEmoji.getId())
        .memeId(memeEmoji.getMeme().getId())
        .emojiId(memeEmoji.getEmoji().getId())
        .status(memeEmoji.getStatus())
        .requestUserNickName(memeEmoji.getRequestUserNickname())
        .build();
  }
}
