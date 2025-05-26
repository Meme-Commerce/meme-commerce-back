package com.example.memecommerceback.domain.categories.service;

import com.example.memecommerceback.domain.categories.converter.CategoryConverter;
import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.entity.Category;
import com.example.memecommerceback.domain.categories.exception.CategoryCustomException;
import com.example.memecommerceback.domain.categories.exception.CategoryExceptionCode;
import com.example.memecommerceback.domain.categories.repository.CategoryRepository;
import com.example.memecommerceback.domain.hashtags.exception.HashtagCustomException;
import com.example.memecommerceback.domain.hashtags.exception.HashtagExceptionCode;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    if(categoryRepository.existsByNameIn(requestDto.getNameList())){
      throw new CategoryCustomException(CategoryExceptionCode.ALREADY_EXIST_NAME);
    }
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
    if(categoryRepository.existsByNameAndIdNot(name, categoryId)){
      throw new HashtagCustomException(HashtagExceptionCode.ALREADY_EXIST_NAME);
    }
    profanityFilterService.validateNoProfanity(name);
    category.update(name);
    return CategoryConverter.toUpdateOneDto(category);
  }

  @Override
  @Transactional
  public void delete(CategoryRequestDto.DeleteDto requestDto) {
    List<Long> requestedIdList = requestDto.getCategoryIdList();
    List<Category> categoryList
        = categoryRepository.findAllById(requestedIdList);

    Set<Long> foundIdList = categoryList.stream()
        .map(Category::getId)
        .collect(Collectors.toSet());

    Set<Long> notFoundIdList = requestedIdList.stream()
        .filter(id -> !foundIdList.contains(id))
        .collect(Collectors.toSet());

    if (!notFoundIdList.isEmpty()) {
      throw new CategoryCustomException(CategoryExceptionCode.NOT_FOUND,
          "요청하신 카테고리 아이디 " + notFoundIdList + "에 대한 카테고리 정보가 없습니다.");
    }

    categoryRepository.deleteAllById(requestedIdList);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoryResponseDto.ReadOneDto> readPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Category> categoryPage = categoryRepository.findAll(pageable);
    return CategoryConverter.toReadPageDto(categoryPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Category findById(Long categoryId){
    return categoryRepository.findById(categoryId).orElseThrow(
        ()-> new CategoryCustomException(CategoryExceptionCode.NOT_FOUND));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Category> findAllById(List<Long> cateogryIdList){
    return categoryRepository.findAllById(cateogryIdList);
  }
}
