package com.example.memecommerceback.global.awsS3.dto;

import com.example.memecommerceback.domain.files.entity.FileExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S3FileResponseDto {

  private String fileName;
  private String originalName;
  private String url;
  private FileExtension extension;
  private Long size;
  private Integer width;
  private Integer height;
}