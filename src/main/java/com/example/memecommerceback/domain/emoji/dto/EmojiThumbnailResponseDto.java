package com.example.memecommerceback.domain.emoji.dto;

import com.example.memecommerceback.domain.images.dto.ImageSummaryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmojiThumbnailResponseDto {
  private Long emojiId;
  private String name;
  private ImageSummaryResponseDto imageSummaryResponseDto;
}