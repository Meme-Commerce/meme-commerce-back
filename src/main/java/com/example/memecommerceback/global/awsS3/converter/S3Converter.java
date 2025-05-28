package com.example.memecommerceback.global.awsS3.converter;

import com.example.memecommerceback.domain.files.entity.FileExtension;
import com.example.memecommerceback.domain.images.entity.ImageExtension;
import com.example.memecommerceback.global.awsS3.dto.S3FileResponseDto;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;

public class S3Converter {

  public static S3ImageResponseDto toS3ImageResponseDto(
      String originalName, ImageExtension ext,
      String fileName, String url, long size) {
    return S3ImageResponseDto.builder()
        .fileName(fileName)
        .originalName(originalName)
        .url(url)
        .extension(ext)
        .size(size)
        .width(0)
        .height(0)
        .build();
  }

  public static S3FileResponseDto toS3FileResponseDto(
      String originalName, FileExtension ext,
      String fileName, String url, long size) {
    return S3FileResponseDto.builder()
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
