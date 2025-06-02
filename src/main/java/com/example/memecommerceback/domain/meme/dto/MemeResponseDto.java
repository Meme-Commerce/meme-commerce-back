package com.example.memecommerceback.domain.meme.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemeResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateDto {
    private List<CreateOneDto> createOneDtoList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {
    private Long memeId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
  }
}
