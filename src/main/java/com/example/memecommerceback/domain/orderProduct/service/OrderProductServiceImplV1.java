package com.example.memecommerceback.domain.orderProduct.service;

import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.orderProduct.exception.OrderProductCustomException;
import com.example.memecommerceback.domain.orderProduct.exception.OrderProductExceptionCode;
import com.example.memecommerceback.domain.orderProduct.repository.OrderProductRepository;
import com.example.memecommerceback.domain.orders.exception.OrderCustomException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderProductServiceImplV1 implements OrderProductServiceV1{

  private final OrderProductRepository orderProductRepository;

  @Override
  @Transactional(readOnly = true)
  public List<OrderProduct> findAllByOrderId(UUID orderId) {
    List<OrderProduct> orderProductList
        = orderProductRepository.findAllByOrderIdFetch(orderId);
    if(orderProductList.isEmpty()){
      throw new OrderProductCustomException(OrderProductExceptionCode.NOT_FOUND);
    }
    return orderProductList;
  }
}
