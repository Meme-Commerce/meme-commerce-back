package com.example.memecommerceback.domain.productCategory.converter;

import com.example.memecommerceback.domain.categories.entity.Category;
import com.example.memecommerceback.domain.productCategory.dto.ProductCategoryResponseDto;
import com.example.memecommerceback.domain.productCategory.entity.ProductCategory;
import com.example.memecommerceback.domain.products.entity.Product;
import java.util.List;

public class ProductCategoryConverter {

  public static List<ProductCategory> toEntityList(
      Product product, List<Category> categoryList) {
    return categoryList.stream().map(category -> {
      return toEntity(product, category);
    }).toList();
  }

  public static ProductCategory toEntity(
      Product product, Category category) {
    return ProductCategory.builder()
        .product(product)
        .category(category)
        .categoryName(category.getName())
        .build();
  }

  public static List<ProductCategoryResponseDto> toResponseDtoList(
      List<ProductCategory> productCategoryList) {
    return productCategoryList.stream().map(
        ProductCategoryConverter::toResponseDto).toList();
  }

  public static ProductCategoryResponseDto toResponseDto(
      ProductCategory productCategory) {
    return ProductCategoryResponseDto.builder()
        .categoryId(productCategory.getCategory().getId())
        .productId(productCategory.getProduct().getId())
        .categoryName(productCategory.getCategoryName())
        .build();
  }
}
