package com.example.memecommerceback.global.awsS3.converter;

import com.example.memecommerceback.domain.images.entity.Extension;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;

public class S3Converter {
  public static S3ResponseDto toS3ResponseDto(
      String originalName, Extension ext,
      String fileName, String url, long size) {
    return S3ResponseDto.builder()
        .fileName(fileName)
        .originalName(originalName)
        .url(url)
        .extension(ext)
        .size(size)
        .width(0)
        .height(0)
        .build();
  }
}
