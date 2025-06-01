package com.example.memecommerceback.domain.images.converter;

import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import com.example.memecommerceback.domain.images.dto.ImageSummaryResponseDto;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.entity.ImageType;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public class ImageConverter {

  public static Image toEntity(
      S3ImageResponseDto s3ResponseDto, User user, ImageType imageType) {
    return Image.builder()
        .prefixUrl(s3ResponseDto.getPrefixUrl())
        .size(s3ResponseDto.getSize())
        .width(s3ResponseDto.getWidth())
        .height(s3ResponseDto.getHeight())
        .extension(s3ResponseDto.getExtension())
        .fileName(s3ResponseDto.getFileName())
        .originalName(s3ResponseDto.getOriginalName())
        .userId(user.getId())
        .imageType(imageType)
        .ownerNickname(user.getNickname())
        .build();
  }

  public static Image toEntity(
      S3ImageResponseDto s3ResponseDto, User user, ImageType imageType,
      Product product) {
    return Image.builder()
        .prefixUrl(s3ResponseDto.getPrefixUrl())
        .size(s3ResponseDto.getSize())
        .width(s3ResponseDto.getWidth())
        .height(s3ResponseDto.getHeight())
        .extension(s3ResponseDto.getExtension())
        .fileName(s3ResponseDto.getFileName())
        .originalName(s3ResponseDto.getOriginalName())
        .userId(user.getId())
        .imageType(imageType)
        .product(product)
        .ownerNickname(user.getNickname())
        .build();
  }
  public static List<Image> toEntityList(
      List<S3ImageResponseDto> s3ResponseDtoList, User user, ImageType imageType, Product product) {
    return s3ResponseDtoList.stream().map(
        s3ResponseDto -> ImageConverter.toEntity(
            s3ResponseDto, user, imageType, product)).toList();
  }

  public static List<ImageResponseDto> toResponseDtoList(List<Image> imageList) {
    return imageList.stream().map(ImageConverter::toResponseDto).toList();
  }

  public static ImageResponseDto toResponseDto(Image image) {
    return ImageResponseDto.builder()
        .imageId(image.getId())
        .extension(image.getExtension())
        .height(image.getHeight())
        .width(image.getWidth())
        .url(image.getPrefixUrl()+image.getFileName())
        .build();
  }

  public static ImageSummaryResponseDto toSummaryResponseDto(Image image){
    return ImageSummaryResponseDto.builder()
        .imageId(image.getId())
        .url(image.getPrefixUrl()+image.getFileName())
        .build();
  }
}
