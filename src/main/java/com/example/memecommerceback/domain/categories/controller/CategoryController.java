package com.example.memecommerceback.domain.categories.controller;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.service.CategoryServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

  private final CategoryServiceV1 categoryService;

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/categories")
  public ResponseEntity<
      CommonResponseDto<CategoryResponseDto.CreateDto>> create(
      @RequestParam CategoryRequestDto.CreateDto requestDto){
    CategoryResponseDto.CreateDto responseDto
        = categoryService.create(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "카테고리 리스트 저장에 성공하셨습니다.",
            HttpStatus.OK.value()));
  }
}
