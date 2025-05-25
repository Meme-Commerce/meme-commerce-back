package com.example.memecommerceback.domain.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "CategoryResponseDto.CreateDto",
      description = "카테고리 여러 개 생성 응답 DTO")
  public static class CreateDto {
    @Schema(description = "생성된 카테고리 정보 리스트")
    private List<CreateOneDto> createOneDtoList;

    @Schema(description = "카테고리 생성 시각", example = "2025-05-28T21:20:40")
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "CategoryResponseDto.CreateOneDto",
      description = "카테고리 하나 생성 정보 DTO")
  public static class CreateOneDto {
    @Schema(description = "카테고리 아이디", example = "1")
    private Long categoryId;

    @Schema(description = "카테고리 명", example = "전자기기")
    private String name;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "CategoryResponseDto.UpdateOneDto",
      description = "카테고리 하나 수정 응답 DTO")
  public static class UpdateOneDto {
    @Schema(description = "카테고리 아이디", example = "1")
    private Long categoryId;

    @Schema(description = "수정된 카테고리 명", example = "의류")
    private String name;

    @Schema(description = "카테고리 수정 시각", example = "2025-05-29T09:10:00")
    private LocalDateTime modifiedAt;
  }
}
