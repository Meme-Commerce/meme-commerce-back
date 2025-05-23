package com.example.memecommerceback.domain.images.converter;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;

public class ImageConverter {

  public static Image toEntity(
      S3ResponseDto s3ResponseDto, User user){
    return Image.builder()
        .url(s3ResponseDto.getUrl())
        .size(s3ResponseDto.getSize())
        .width(s3ResponseDto.getWidth())
        .height(s3ResponseDto.getHeight())
        .extension(s3ResponseDto.getExtension())
        .fileName(s3ResponseDto.getFileName())
        .originalName(s3ResponseDto.getOriginalName())
        .userId(user.getId())
        .ownerNickname(user.getNickname())
        .build();
  }
}
