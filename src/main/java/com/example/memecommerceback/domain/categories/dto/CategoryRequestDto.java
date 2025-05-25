package com.example.memecommerceback.domain.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryRequestDto {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateDto {
    @NotNull(message = "이름 리스트는 필수 입력 값입니다.")
    @Size(min = 1, message = "최소 1개의 카테고리명을 입력해야 합니다.")
    @Schema(description = "삭제할 카테고리 ID 리스트", example = "[지성, 상혁, 허수]")
    private List<
        @Pattern(regexp = "^[가-힣]{1,20}$",
            message = "한글로 1~20자까지 입력 가능합니다.")
            String> nameList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DeleteDto {
    @NotNull(message = "삭제할 카테고리 아이디 리스트는 필수 입력 값입니다.")
    @Size(min = 1, message = "최소 1개의 카테고리 아이디를 입력해야 합니다.")
    @Schema(description = "삭제할 카테고리 ID 리스트", example = "[1,2,3]")
    private List<Long> categoryIdList;
  }
}
