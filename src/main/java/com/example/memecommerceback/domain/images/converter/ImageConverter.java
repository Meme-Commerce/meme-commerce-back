package com.example.memecommerceback.domain.images.converter;

import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import java.util.List;

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

  public static List<Image> toEntityList(
      List<S3ResponseDto> s3ResponseDtoList, User user){
    return s3ResponseDtoList.stream().map(
        s3ResponseDto -> ImageConverter.toEntity(s3ResponseDto, user)).toList();
  }

  public static List<ImageResponseDto> toResponseDtoList(List<Image> imageList){
    return imageList.stream().map(ImageConverter::toResponseDto).toList();
  }

  public static ImageResponseDto toResponseDto(Image image){
    return ImageResponseDto.builder()
        .imageId(image.getId())
        .extension(image.getExtension())
        .height(image.getHeight())
        .width(image.getWidth())
        .url(image.getUrl())
        .build();
  }
}
