package com.example.memecommerceback.domain.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductRequestDto {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RegisterOneDto {
    @Size(min = 1, max = 30,
        message = "상품명은 최소 1자부터 30자를 입력하셔야합니다.")
    @NotNull(message = "상품 명은 필수 입력란입니다.")
    private String name;
    @Min(value = 1, message = "재고 최소 재고량은 1개입니다.")
    @NotNull(message = "재고는 필수 입력란입니다.")
    private Long stock;
    @Positive(message = "음수 또는 0은 사용할 수 없고 최소 100원 부터 입력 가능합니다.")
    @Min(value = 100, message = "최소 가격은 100원 이상입니다.")
    private Long price;
    @NotNull(message = "설명은 필수 입력란입니다.")
    private String description;
    @NotNull(message = "판매 시작 일은 필수 입력란입니다.")
    private LocalDateTime sellStartDate;
    @NotNull(message = "판매 마감 일은 필수 입력란입니다.")
    private LocalDateTime sellEndDate;
  }
}
