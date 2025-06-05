package com.example.memecommerceback.domain.orderProduct.repository;


import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {

  @Query("select op from OrderProduct op " +
      "join fetch op.order " +
      "join fetch op.product " +
      "where op.order.id = :orderId")
  List<OrderProduct> findAllByOrderIdFetch(@Param("orderId") UUID orderId);
}


