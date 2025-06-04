package com.example.memecommerceback.domain.orders.dto;

import com.example.memecommerceback.domain.orderProduct.dto.OrderProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
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

    private List<OrderProductRequestDto.CreateOneDto> products;
  }
}
