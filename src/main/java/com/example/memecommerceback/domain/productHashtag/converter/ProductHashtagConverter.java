package com.example.memecommerceback.domain.productHashtag.converter;

import com.example.memecommerceback.domain.productHashtag.dto.ProductHashtagResponseDto;
import com.example.memecommerceback.domain.productHashtag.entity.ProductHashtag;
import java.util.List;

public class ProductHashtagConverter {

  public static List<ProductHashtagResponseDto> toResponseDtoList(
      List<ProductHashtag> productHashtagList) {
    return productHashtagList.stream()
        .map(ProductHashtagConverter::toResponseDto)
        .toList();
  }

  public static ProductHashtagResponseDto toResponseDto(ProductHashtag productHashtag) {
    return ProductHashtagResponseDto.builder()
        .productId(productHashtag.getProduct().getId())
        .hashtagId(productHashtag.getHashtag().getId())
        .hashtagName(productHashtag.getHashtagName())
        .createdAt(productHashtag.getCreatedAt())
        .build();
  }
}
