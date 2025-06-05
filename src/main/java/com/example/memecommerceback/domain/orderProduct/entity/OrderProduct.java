package com.example.memecommerceback.domain.orderProduct.entity;

import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.products.entity.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_product")
public class OrderProduct {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @JoinColumn(name = "product_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Product product;

  @JoinColumn(name = "order_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Order order;

  @Column(nullable = false)
  private Long quantity;

  @Column(nullable = false)
  private BigDecimal price;
}

