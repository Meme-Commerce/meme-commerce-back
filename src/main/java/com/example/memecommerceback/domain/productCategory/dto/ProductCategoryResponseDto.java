package com.example.memecommerceback.domain.productCategory.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryResponseDto {

  private UUID productId;
  private Long categoryId;
  private String categoryName;
  private LocalDateTime createdAt;
}
