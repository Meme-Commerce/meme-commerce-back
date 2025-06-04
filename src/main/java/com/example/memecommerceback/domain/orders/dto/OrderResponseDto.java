package com.example.memecommerceback.domain.orders.dto;

import com.example.memecommerceback.domain.orders.entity.OrderStatus;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {

    private List<ProductResponseDto.ReadOneDto> orderedProductList;
    private Long totalPrice;
    private String orderNumber;
    private OrderStatus orderStatus;
    private String purchaserNickname;
  }
}
