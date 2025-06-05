package com.example.memecommerceback.domain.orders.repository;


import com.example.memecommerceback.domain.orders.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  @Query("select o from Order o "
      + "join fetch o.orderProductList op "
      + "join fetch op.product p "
      + "where o.id = :orderId")
  Order findByIdWithAllDetails(@Param("orderId") UUID orderId);

  @EntityGraph(attributePaths = {
      "orderProductList",
      "orderProductList.product",
      "orderProductList.product.owner",
      "orderProductList.product.imageList",
      "orderProductList.product.productCategoryList",
      "orderProductList.product.productCategoryList.category",
      "orderProductList.product.productHashtagList",
      "orderProductList.product.productHashtagList.hashtag"
  })
  @Query("select o from Order o where o.id = :orderId")
  Order findByIdWithGraph(@Param("orderId") UUID orderId);
}
