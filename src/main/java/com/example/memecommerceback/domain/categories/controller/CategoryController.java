package com.example.memecommerceback.domain.categories.controller;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.service.CategoryServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Category API", description = "카테고리 관리 API")
public class CategoryController {

  private final CategoryServiceV1 categoryService;

  @Operation(summary = "카테고리(여러 개) 생성", description = "관리자가 카테고리 이름 리스트를 받아 여러 개를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리 리스트 저장 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponseDto.CreateDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/categories")
  public ResponseEntity<CommonResponseDto<CategoryResponseDto.CreateDto>> create(
      @RequestBody @Valid CategoryRequestDto.CreateDto requestDto) {
    CategoryResponseDto.CreateDto responseDto = categoryService.create(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "카테고리 리스트 저장에 성공하셨습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "카테고리 단일 수정", description = "관리자가 카테고리의 이름을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponseDto.UpdateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/categories/{categoryId}")
  public ResponseEntity<CommonResponseDto<CategoryResponseDto.UpdateOneDto>> updateOne(
      @PathVariable Long categoryId,
      @RequestParam @NotNull(message = "카테고리 이름은 필수 입력 값입니다.")
      @Pattern(regexp = "^[가-힣]{1,20}$", message = "한글로 1~20자까지 입력 가능합니다.")
      String name) {
    CategoryResponseDto.UpdateOneDto responseDto = categoryService.updateOne(categoryId, name);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "카테고리 하나 수정 성공하셨습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "카테고리(여러 개) 삭제", description = "관리자가 카테고리 ID 리스트로 여러 개를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommonResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/categories/delete")
  public ResponseEntity<CommonResponseDto<Void>> delete(
      @RequestBody @Valid CategoryRequestDto.DeleteDto requestDto) {
    categoryService.delete(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(null, "카테고리 리스트를 삭제 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "카테고리 페이지 조회",
      description = "모든 사용자는 카테고리 페이지를 조회할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  @GetMapping("/categories")
  public ResponseEntity<CommonResponseDto<Page<CategoryResponseDto.ReadOneDto>>> readPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Page<CategoryResponseDto.ReadOneDto> responseDto
        = categoryService.readPage(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "카테고리 페이지를 조회 하였습니다.", HttpStatus.OK.value()));
  }
}
