package com.example.memecommerceback.global.awsS3.dto;

import com.example.memecommerceback.domain.images.entity.ImageExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S3ImageResponseDto {

  private String originalName;
  private String prefixUrl;
  private String fileName;
  private ImageExtension extension;
  private Long size;
  private Integer width;
  private Integer height;
}
