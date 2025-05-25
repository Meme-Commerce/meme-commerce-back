package com.example.memecommerceback.domain.categories.service;

import com.example.memecommerceback.domain.categories.converter.CategoryConverter;
import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.entity.Category;
import com.example.memecommerceback.domain.categories.repository.CategoryRepository;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplV1 implements CategoryServiceV1{

  private final ProfanityFilterService profanityFilterService;
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional
  public CategoryResponseDto.CreateDto create(
      CategoryRequestDto.CreateDto requestDto) {
    // 1. 공란인지?, 욕설이 들어갔는지?
    profanityFilterService.validateListNoProfanity(requestDto.getNameList());
    List<Category> categoryList
        = CategoryConverter.toEntityList(requestDto);
    categoryRepository.saveAll(categoryList);
    return CategoryConverter.toCreateDto(categoryList);
  }
}
