package com.example.memecommerceback.domain.images.dto;

import com.example.memecommerceback.domain.images.entity.ImageExtension;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageSummaryResponseDto {
  private UUID imageId;
  private String url;
}
