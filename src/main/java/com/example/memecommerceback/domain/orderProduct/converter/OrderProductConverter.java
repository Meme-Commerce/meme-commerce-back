package com.example.memecommerceback.domain.orderProduct.converter;

import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.products.converter.ProductConverter;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.entity.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderProductConverter {
  public static List<OrderProduct> toEntityList(
      List<Product> productList, List<BigDecimal> priceList,
      List<Long> quantityList, Order order) {
    List<OrderProduct> result = new ArrayList<>();
    for (int i = 0; i < productList.size(); i++) {
      result.add(
          toEntity(productList.get(i), priceList.get(i),
              quantityList.get(i), order));
    }
    return result;
  }

  public static OrderProduct toEntity(
      Product product, BigDecimal price, Long quantity, Order order) {
    return OrderProduct.builder()
        .order(order)
        .product(product)
        .quantity(quantity)
        .price(price)
        .build();
  }

  public static List<ProductResponseDto.ReadOneDto> toReadListDto(
      List<OrderProduct> orderProductList){
    return orderProductList.stream()
        .map(OrderProduct::getProduct)
        .map(ProductConverter::toReadOneDto)
        .toList();
  }
}
