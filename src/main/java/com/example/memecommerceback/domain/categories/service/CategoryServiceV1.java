package com.example.memecommerceback.domain.categories.service;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.entity.Category;

public interface CategoryServiceV1 {

  CategoryResponseDto.CreateDto create(
      CategoryRequestDto.CreateDto requestDto);

  CategoryResponseDto.UpdateOneDto updateOne(
      Long categoryId, String name);

  Category findById(Long categoryId);
}
