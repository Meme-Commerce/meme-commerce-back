package com.example.memecommerceback.domain.orders.entity;

import com.example.memecommerceback.domain.orders.exception.OrderCustomException;
import com.example.memecommerceback.domain.orders.exception.OrderExceptionCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public enum OrderStatus {
  /**
   * 주문 성공 프로세스 PENDING -> WAITING_FOR_PAYMENT -> PAID -> SHIPPED -> DELIVERED 주문 실패 프로세스 (CASE 1:
   * 결제 실패) PENDING -> WAITING_FOR_PAYMENT -> FAILED 주문 실패 프로세스 (CASE 2: 배송 중 또는 검수 중일 때 환불 요청)
   * PENDING || SHIPPED -> REFUNDED -> CANCELED
   */
  PAID(Status.PAID),              // 결제가 완료된 상태
  SHIPPED(Status.SHIPPED),        // 상품이 배송 중인 상태
  DELIVERED(Status.DELIVERED),    // 상품이 고객에게 배송 완료된 상태
  CANCELED(Status.CANCELED),      // 결제를 하기 전에 주문이 취소된 상태
  REFUNDED(Status.REFUNDED),      // 환불이 완료된 상태
  FAILED(Status.FAILED),          // 결제 또는 주문 처리에서 실패한 상태
  WAITING_FOR_PAYMENT(Status.WAITING_FOR_PAYMENT), // 결제 대기중 상태 추가
  WAITING_FOR_REFUNDED(Status.WAITING_FOR_REFUNDED);

  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public static class Status {

    public static final String PENDING = "PENDING";
    public static final String PAID = "PAID";
    public static final String SHIPPED = "SHIPPED";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELED = "CANCELED";
    public static final String REFUNDED = "REFUNDED";
    public static final String FAILED = "FAILED";
    public static final String WAITING_FOR_PAYMENT = "WAITING_FOR_PAYMENT";
    public static final String WAITING_FOR_REFUNDED = "WAITING_FOR_REFUNDED";
  }

  public static OrderStatus fromStatus(String status) {
    for (OrderStatus orderStatus : OrderStatus.values()) {
      if (orderStatus.getStatus().equals(status)) {
        return orderStatus;
      }
    }
    throw new OrderCustomException(OrderExceptionCode.NOT_EXIST_STATUS);
  }

  public static List<OrderStatus> fromStatusList(List<String> statusList) {
    List<OrderStatus> orderStatusList = new ArrayList<>();
    for (OrderStatus orderStatus : OrderStatus.values()) {
      for (String status : statusList) {
        if (orderStatus.getStatus().equals(status)) {
          orderStatusList.add(orderStatus);
        }
      }
      return orderStatusList;
    }
    throw new OrderCustomException(OrderExceptionCode.NOT_EXIST_STATUS);
  }

  public static Set<OrderStatus> getAlreadyCompletedList() {
    return Set.of(
        OrderStatus.CANCELED, OrderStatus.FAILED, OrderStatus.DELIVERED,
        OrderStatus.PAID, OrderStatus.REFUNDED);
  }
}