package com.example.memecommerceback.domain.orders.entity;

import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "orders")
public class Order extends CommonEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private BigDecimal totalPrice = BigDecimal.ZERO;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.WAITING_FOR_PAYMENT;

  @Column(nullable = false)
  private String orderNumber;

  // 주문자
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User purchaser;

  // 주문한 상품
  @Builder.Default
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  List<OrderProduct> orderProductList = new ArrayList<>();

  @Builder.Default
  private LocalDateTime deliveredAt = null;

  public void resetOrderProductList(List<OrderProduct> orderProductList) {
    if (!this.orderProductList.isEmpty()) {
      this.orderProductList.clear();
    }
    this.orderProductList.addAll(orderProductList);
  }
}

