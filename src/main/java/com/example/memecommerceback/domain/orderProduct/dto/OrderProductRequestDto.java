package com.example.memecommerceback.domain.orderProduct.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderProductRequestDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "OrderProductRequestDto.CreateOneDto",
      description = "상품 주문 요청 DTO")
  public static class CreateOneDto {
    @NotNull(message = "상품 식별자는 필수 입력란입니다.")
    private UUID productId;
    @NotNull(message = "수량은 필수 입력란입니다.")
    @Min(value = 1, message = "수량은 최소 1개 이상 입력하셔야합니다.")
    private Long quantity;
  }
}
