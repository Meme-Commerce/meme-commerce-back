package com.example.memecommerceback.domain.emoji.dto;

import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmojiResponseDto {

  private Long emojiId;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private ImageResponseDto imageResponseDto;
}
