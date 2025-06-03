package com.example.memecommerceback.domain.memeEmoji.converter;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import org.springframework.data.domain.Page;

public class MemeEmojiConverter {

  public static MemeEmoji toEntity(
      Meme meme, Emoji emoji, String message, String name, String requestUserNickname){
    return MemeEmoji.builder()
        .meme(meme).emoji(emoji).message(message).name(name)
        .requestUserNickname(requestUserNickname).build();
  }

  public static MemeEmojiResponseDto.CreateOneDto toCreateOneDto(
      MemeEmoji memeEmoji, Long memeId, Long emojiId){
    return MemeEmojiResponseDto.CreateOneDto.builder()
        .memeEmojiId(memeEmoji.getId())
        .memeId(memeId).emojiId(emojiId)
        .name(memeEmoji.getName())
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
        .name(memeEmoji.getName())
        .status(memeEmoji.getStatus())
        .requestUserNickName(memeEmoji.getRequestUserNickname())
        .build();
  }

  public static Page<MemeEmojiResponseDto.ReadOneDto> toReadPageDto(
      Page<MemeEmoji> memeEmojiPage){
    return memeEmojiPage.map(MemeEmojiConverter::toReadOneDto);
  }

  public static MemeEmojiResponseDto.ReadOneDto toReadOneDto(
      MemeEmoji memeEmoji){
    return MemeEmojiResponseDto.ReadOneDto.builder()
        .memeEmojiId(memeEmoji.getId())
        .memeId(memeEmoji.getMeme().getId())
        .emojiId(memeEmoji.getEmoji().getId())
        .name(memeEmoji.getName())
        .status(memeEmoji.getStatus())
        .requestUserNickName(memeEmoji.getRequestUserNickname())
        .build();
  }
}
