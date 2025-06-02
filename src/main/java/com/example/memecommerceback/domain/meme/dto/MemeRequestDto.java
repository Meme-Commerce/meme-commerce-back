package com.example.memecommerceback.domain.meme.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemeRequestDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateDto {
    List<CreateOneDto> createOneDtoList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {
    private String name;
    private String description;
  }
}
