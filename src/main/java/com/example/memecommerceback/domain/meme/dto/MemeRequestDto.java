package com.example.memecommerceback.domain.meme.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "밈 생성 DTO는 필수 입력란입니다.")
    @Schema(description = "밈 생성 DTO", example = "[name, description]")
    List<CreateOneDto> createOneDtoList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {
    @Size(min = 1, max = 30,
        message = "상품명은 최소 1자부터 30자를 입력하셔야합니다.")
    @NotNull(message = "밈 이름은 필수 입력란입니다.")
    @Schema(description = "밈 이름", example = "트랄라레로 트랄랄라")
    private String name;
    @Size(min = 1, max = 100,
        message = "밈의 설명은 최소 1자부터 100자를 입력하셔야합니다.")
    @NotNull(message = "밈 설명은 필수 입력란입니다.")
    @Schema(description = "밈의 설명(원작자 필수 기입)", example = "이탈리안 봇")
    private String description;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateOneDto {
    @Size(min = 1, max = 30,
        message = "상품명은 최소 1자부터 30자를 입력하셔야합니다.")
    @Schema(description = "밈 이름", example = "트랄라레로 트랄랄라")
    private String name;
    @Size(min = 1, max = 100,
        message = "밈의 설명은 최소 1자부터 100자를 입력하셔야합니다.")
    @Schema(description = "밈의 설명(원작자 필수 기입)", example = "이탈리안 봇")
    private String description;
  }
}
