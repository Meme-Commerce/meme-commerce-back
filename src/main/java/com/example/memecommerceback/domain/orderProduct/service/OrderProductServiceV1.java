package com.example.memecommerceback.domain.orderProduct.service;

import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import java.util.List;
import java.util.UUID;

public interface OrderProductServiceV1 {

  List<OrderProduct> findAllByOrderId(UUID orderId);

  OrderProduct findByOrderId(UUID orderId);
}
