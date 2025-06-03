package com.example.memecommerceback.domain.memeEmoji.dto;

import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmojiStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemeEmojiResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeEmojiResponseDto.CreateOneDto", description = "밈모지(밈-이모지 연결) 생성 응답 DTO")
  public static class CreateOneDto {
    @Schema(description = "밈모지 고유 식별자", example = "1001")
    private Long memeEmojiId;

    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "이모지 아이디", example = "21")
    private Long emojiId;

    @Schema(description = "밈모지 이름", example = "치즈 피자와 이탈리안 브레인 봇")
    private String name;

    @Schema(description = "사용자 요청 메시지", example = "이 밈에 이 이모지를 꼭 넣어주세요!")
    private String requestMessage;

    @Schema(description = "요청자 닉네임", example = "memeUser01")
    private String requestUserNickname;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeEmojiResponseDto.UpdateOneDto", description = "밈모지 상태/내용 수정 응답 DTO")
  public static class UpdateOneDto {
    @Schema(description = "밈모지 고유 식별자", example = "1001")
    private Long memeEmojiId;

    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "이모지 아이디", example = "21")
    private Long emojiId;

    @Schema(description = "밈모지 이름", example = "치즈 피자와 이탈리안 브레인 봇")
    private String name;

    @Schema(description = "요청자 닉네임", example = "memeUser01")
    private String requestUserNickname;

    @Schema(description = "밈모지 상태", example = "APPROVED")
    private MemeEmojiStatus status;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeEmojiResponseDto.ReadOneDto", description = "밈모지(밈-이모지 연결) 단일 조회 응답 DTO")
  public static class ReadOneDto {
    @Schema(description = "밈모지 고유 식별자", example = "1001")
    private Long memeEmojiId;

    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "이모지 아이디", example = "21")
    private Long emojiId;

    @Schema(description = "밈모지 상태", example = "PENDING")
    private MemeEmojiStatus status;

    @Schema(description = "밈모지 이름", example = "치즈 피자와 이탈리안 브레인 봇")
    private String name;

    @Schema(description = "요청자 닉네임", example = "memeUser01")
    private String requestUserNickname;
  }
}
