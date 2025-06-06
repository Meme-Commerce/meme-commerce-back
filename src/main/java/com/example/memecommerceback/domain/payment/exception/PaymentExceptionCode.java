package com.example.memecommerceback.domain.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentExceptionCode {
  NOT_FOUND(
      HttpStatus.NOT_FOUND, "PAYMENT-001",
      "찾을 수 없는 결제입니다."),
  TOSS_PAYMENT_REQUEST_FAIL(
      HttpStatus.BAD_REQUEST, "PAYMENT-002",
      "Toss 결제 요청 실패"),
  UNKNOWN_STATUS(
      HttpStatus.BAD_REQUEST, "PAYMENT-003",
      "알 수 없는 상태입니다."),
  TOSS_PAYMENT_CANCEL_FAIL(
      HttpStatus.BAD_REQUEST, "PAYMENT-004",
      "Toss 결제 취소 실패"),
  ALREADY_CANCELED_PAYMENT(
      HttpStatus.BAD_REQUEST, "PAYMENT-005",
      "이미 결제를 취소하였습니다."),
  NOT_WAITING_FOR_PAYMENT_ORDER(
      HttpStatus.BAD_REQUEST, "PAYMENT-006",
      "이 상품은 결제 대기중인 주문이 아닙니다."),
  ;
  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
