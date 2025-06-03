package com.example.memecommerceback.domain.memeEmoji.dto;

import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmojiStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemeEmojiResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {
    private Long memeEmojiId;
    private Long memeId;
    private Long emojiId;
    private String name;
    private String requestMessage;
    private String requestUserNickName;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateOneDto {
    private Long memeEmojiId;
    private Long memeId;
    private Long emojiId;
    private String name;
    private String requestUserNickName;
    private MemeEmojiStatus status;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ReadOneDto {
    private Long memeEmojiId;
    private Long memeId;
    private Long emojiId;
    private MemeEmojiStatus status;
    private String name;
    private String requestUserNickName;
  }
}
