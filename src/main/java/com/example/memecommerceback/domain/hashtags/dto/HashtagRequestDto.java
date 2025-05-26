package com.example.memecommerceback.domain.hashtags.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HashtagRequestDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateDto {

    @NotNull(message = "이름 리스트는 필수 입력 값입니다.")
    @Size(min = 1, message = "최소 1개의 해시태그명을 입력해야 합니다.")
    @Schema(description = "생성할 해시태그명 리스트", example = "[\"밈\", \"유머\", \"트렌드\"]")
    private List<
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$",
            message = "한글/영문/숫자로 1~20자까지 입력 가능합니다.")
            String> nameList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DeleteDto {

    @NotNull(message = "삭제할 해시태그 아이디 리스트는 필수 입력 값입니다.")
    @Size(min = 1, message = "최소 1개의 해시태그 아이디를 입력해야 합니다.")
    @Schema(description = "삭제할 해시태그 ID 리스트", example = "[1,2,3]")
    private List<Long> hashtagIdList;
  }
}
