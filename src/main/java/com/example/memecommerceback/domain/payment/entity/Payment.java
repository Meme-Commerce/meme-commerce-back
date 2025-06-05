package com.example.memecommerceback.domain.payment.entity;

import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  // Toss 결제 고유 키
  @Column(nullable = false, unique = true)
  private String paymentKey;

  // 총 결제 금액
  @Column(nullable = false)
  private Long amount;

  // 결제 수단 (카드, 계좌이체 등)
  @Column(nullable = false)
  private String method;

  private String cardCompany;

  private String cardNumber;

  @Column(nullable = false)
  private LocalDateTime approvedAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentStatus status; // 결제 상태 (SUCCESS, FAIL 등)

  @OneToOne(fetch = FetchType.LAZY)
  private Order order;

  public void updateStatus(PaymentStatus status) {
    this.status = status;
  }
}
