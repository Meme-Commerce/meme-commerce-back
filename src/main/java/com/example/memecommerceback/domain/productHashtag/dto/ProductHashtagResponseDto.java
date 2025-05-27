package com.example.memecommerceback.domain.productHashtag.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHashtagResponseDto {

  private UUID productId;
  private Long hashtagId;
  private String hashtagName;
  private LocalDateTime createdAt;
}
