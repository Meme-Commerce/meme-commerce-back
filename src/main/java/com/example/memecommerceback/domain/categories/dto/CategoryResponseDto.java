package com.example.memecommerceback.domain.categories.dto;

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
  public static class CreateDto {
    private List<CreateOneDto> createOneDtoList;
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {
    private Long categoryId;
    private String name;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateOneDto {
    private Long categoryId;
    private String name;
    private LocalDateTime modifiedAt;
  }
}
