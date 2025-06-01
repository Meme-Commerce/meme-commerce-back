package com.example.memecommerceback.global.awsS3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S3ImageSummaryResponseDto {
  private String prefixUrl;
  private String imageName;
}
