package com.example.memecommerceback.domain.orders.converter;

import com.example.memecommerceback.domain.orderProduct.converter.OrderProductConverter;
import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.orders.dto.OrderResponseDto;
import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.products.converter.ProductConverter;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;

public class OrderConverter {

  public static Order toEntity(
      User user, Long totalPrice, String orderNumber) {
    return Order.builder()
        .purchaser(user)
        .totalPrice(totalPrice)
        .orderNumber(orderNumber)
        .build();
  }

  public static OrderResponseDto.CreateOneDto toCreateOneDto(
      Order order, List<OrderProduct> orderProductList, String purchaserNickname) {
    return OrderResponseDto.CreateOneDto.builder()
        .orderNumber(order.getOrderNumber())
        .orderStatus(order.getStatus())
        .totalPrice(order.getTotalPrice())
        .orderedProductList(OrderProductConverter.toReadListDto(orderProductList))
        .purchaserNickname(purchaserNickname)
        .build();
  }
}
