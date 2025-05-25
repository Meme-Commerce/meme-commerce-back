package com.example.memecommerceback.domain.categories.service;

import com.example.memecommerceback.domain.categories.converter.CategoryConverter;
import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.entity.Category;
import com.example.memecommerceback.domain.categories.exception.CategoryCustomException;
import com.example.memecommerceback.domain.categories.exception.CategoryExceptionCode;
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

  @Override
  @Transactional
  public CategoryResponseDto.UpdateOneDto updateOne(
      Long categoryId, String name) {
    Category category = findById(categoryId);
    profanityFilterService.validateNoProfanity(name);
    category.update(name);
    return CategoryConverter.toUpdateOneDto(category);
  }

  @Override
  @Transactional(readOnly = true)
  public Category findById(Long categoryId){
    return categoryRepository.findById(categoryId).orElseThrow(
        ()-> new CategoryCustomException(CategoryExceptionCode.NOT_FOUND));
  }
}
