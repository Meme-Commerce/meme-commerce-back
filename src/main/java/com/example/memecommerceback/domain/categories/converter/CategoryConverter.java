package com.example.memecommerceback.domain.categories.converter;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto.CreateDto;
import com.example.memecommerceback.domain.categories.entity.Category;
import com.example.memecommerceback.domain.productCategory.entity.ProductCategory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

public class CategoryConverter {

  public static Category toEntity(String name){
    return Category.builder().name(name).build();
  }

  public static List<Category> toEntityList(
      CategoryRequestDto.CreateDto requestDto){
    return requestDto.getNameList().stream()
        .map(CategoryConverter::toEntity)
        .toList();
  }

  public static CategoryResponseDto.CreateDto toCreateDto(
      List<Category> categoryList){
    LocalDateTime createdAt = categoryList.stream()
        .map(Category::getCreatedAt)
        .max(LocalDateTime::compareTo)
        .orElse(null);

    List<CategoryResponseDto.CreateOneDto> createOneDtoList
        =  categoryList.stream().map(CategoryConverter::toCreateOneDto).toList();

    return CreateDto.builder().createOneDtoList(createOneDtoList)
        .createdAt(createdAt)
        .build();
  }

  public static CategoryResponseDto.CreateOneDto toCreateOneDto(
      Category category){
    return CategoryResponseDto.CreateOneDto.builder()
        .categoryId(category.getId())
        .name(category.getName())
        .build();
  }

  public static CategoryResponseDto.UpdateOneDto toUpdateOneDto(Category category){
    return CategoryResponseDto.UpdateOneDto.builder()
        .categoryId(category.getId())
        .name(category.getName())
        .modifiedAt(category.getModifiedAt())
        .build();
  }

  public static Page<CategoryResponseDto.ReadOneDto> toReadPageDto(
      Page<Category> categoryPage){
    return categoryPage.map(CategoryConverter::toReadOneDto);
  }

  public static CategoryResponseDto.ReadOneDto toReadOneDto(Category category){
    return CategoryResponseDto.ReadOneDto.builder()
        .categoryId(category.getId())
        .name(category.getName())
        .build();
  }

  public static List<CategoryResponseDto.ReadOneDto> toReadDtoList(
      List<ProductCategory> productCategoryList){
    return productCategoryList.stream()
        .map(ProductCategory::getCategory)
        .map(CategoryConverter::toReadOneDto)
        .toList();
  }
}
