package com.example.memecommerceback.domain.images.dto;

import com.example.memecommerceback.domain.images.entity.ImageExtension;
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
@Schema(name = "ImageResponseDto", description = "이미지 응답 DTO")
public class ImageResponseDto {
  @Schema(description = "이미지 식별자",
      example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
  private UUID imageId;
  @Schema(description = "이미지 url", example = "/user/usernickname/product/")
  private String url;
  @Schema(description = "이미지 확장자", example = "png")
  private ImageExtension extension;
  @Schema(description = "이모지 높이", example = "200")
  private int height;
  @Schema(description = "이모지 너비", example = "200")
  private int width;
}
