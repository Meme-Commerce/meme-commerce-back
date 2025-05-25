package com.example.memecommerceback.domain.categories.controller;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.service.CategoryServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
      @RequestBody @Valid CategoryRequestDto.CreateDto requestDto){
    CategoryResponseDto.CreateDto responseDto
        = categoryService.create(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "카테고리 리스트 저장에 성공하셨습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/categories/{categoryId}")
  public ResponseEntity<
      CommonResponseDto<CategoryResponseDto.UpdateOneDto>> updateOne(
          @PathVariable Long categoryId,
      @RequestParam @NotNull(message = "카테고리 이름은 필수 입력 값입니다.")
      @Pattern(regexp = "^[가-힣]{1,20}$",
          message = "한글로 1~20자까지 입력 가능합니다.") String name){
    CategoryResponseDto.UpdateOneDto responseDto
        = categoryService.updateOne(categoryId, name);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "카테고리 하나 수정 성공하셨습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/categories/delete")
  public ResponseEntity<CommonResponseDto<Void>> delete(
          @RequestBody @Valid CategoryRequestDto.DeleteDto requestDto){
    categoryService.delete(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null, "카테고리 리스트를 삭제 하였습니다.",
            HttpStatus.OK.value()));
  }
}
