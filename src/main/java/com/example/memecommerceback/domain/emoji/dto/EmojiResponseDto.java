package com.example.memecommerceback.domain.emoji.dto;

import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EmojiResponseDto", description = "이모지 응답 DTO")
public class EmojiResponseDto {
  @Schema(description = "이모지 식별자", example = "1L")
  private Long emojiId;
  @Schema(description = "이모지 이름 또는 설명", example = "고양이 눕는 자세")
  private String name;
  @Schema(description = "생성 시각", example = "2025-05-15T10:15:30")
  private LocalDateTime createdAt;
  @Schema(description = "수정 시각", example = "2025-05-16T10:15:30")
  private LocalDateTime modifiedAt;
  @Schema(description = "이모지 이미지 리스트",
      example = "[imageId, url, extension, width, height]")
  private ImageResponseDto emojiImageResponseDto;
}
