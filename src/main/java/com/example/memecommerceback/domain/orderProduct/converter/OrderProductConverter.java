package com.example.memecommerceback.domain.orderProduct.converter;

import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.products.converter.ProductConverter;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.entity.Product;
import java.util.List;

public class OrderProductConverter {
  public static List<OrderProduct> toEntityList(
      List<Product> productList, Order order){
    return productList.stream().map(
        product -> toEntity(product, order)).toList();
  }

  public static OrderProduct toEntity(
      Product product, Order order){
    return OrderProduct.builder()
        .order(order).product(product).build();
  }

  public static List<ProductResponseDto.ReadOneDto> toReadListDto(
      List<OrderProduct> orderProductList){
    return orderProductList.stream()
        .map(OrderProduct::getProduct)
        .map(ProductConverter::toReadOneDto)
        .toList();
  }
}
