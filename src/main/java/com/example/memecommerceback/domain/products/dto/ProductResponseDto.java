package com.example.memecommerceback.domain.products.dto;

import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductResponseDto {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RegisterOneDto {
    private UUID productId;
    private String name;
    private String description;
    private Long price;
    private String ownerName;
    private List<ImageResponseDto> imageResponseDtoList;
    // private List<CategoryResponseDto.ReadOneDto> categoryList;
    // private List<HashtagResponseDto.ReadOneDto> hashtagList;
    private Long stock;
    private String registeredCompanyName;
    private ProductStatus status;
    private LocalDateTime createdAt;
  }
}
