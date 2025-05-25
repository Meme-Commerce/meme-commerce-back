package com.example.memecommerceback.domain.categories.service;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;

public interface CategoryServiceV1 {

  CategoryResponseDto.CreateDto create(
      CategoryRequestDto.CreateDto requestDto);
}
