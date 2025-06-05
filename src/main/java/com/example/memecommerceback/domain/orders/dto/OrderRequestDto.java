package com.example.memecommerceback.domain.orders.dto;

import com.example.memecommerceback.domain.orderProduct.dto.OrderProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderRequestDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "OrderRequestDto.CreateOneDto",
      description = "주문 요청 DTO")
  public static class CreateOneDto {

    @Valid
    @NotNull(message = "상품 리스트는 필수입니다.")
    @NotEmpty(message = "최소 하나의 상품을 선택해야 합니다.")
    @Schema(description = "주문할 상품 리스트")
    private List<OrderProductRequestDto.CreateOneDto> products;
  }
}
