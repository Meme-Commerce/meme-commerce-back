package com.example.memecommerceback.domain.emoji.dto;

import com.example.memecommerceback.domain.images.dto.ImageSummaryResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EmojiThumbnailResponseDto", description = "이모지 섬네일 응답 DTO")
public class EmojiThumbnailResponseDto {
  @Schema(description = "이모지 식별자", example = "1L")
  private Long emojiId;
  @Schema(description = "이모지 이름 또는 설명", example = "고양이 눕는 자세")
  private String name;
  @Schema(description = "이모지 이미지 리스트", example = "[]")
  private ImageSummaryResponseDto imageSummaryResponseDto;
}