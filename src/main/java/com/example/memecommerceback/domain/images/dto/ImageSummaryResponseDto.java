package com.example.memecommerceback.domain.images.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ImageSummaryResponseDto", description = "이미지 섬네일 응답 DTO")
public class ImageSummaryResponseDto {
  @Schema(description = "이미지 식별자",
      example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
  private UUID imageId;
  @Schema(description = "이미지 url", example = "/user/usernickname/product/")
  private String url;
}
