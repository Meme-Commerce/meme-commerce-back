package com.example.memecommerceback.domain.orderProduct.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderProductExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "ORDER-PRODUCT-001",
      "해당 상품을 주문하신 기록이 없습니다."),
  NOT_MATCHED(
      HttpStatus.BAD_REQUEST, "ORDER-PRODUCT-002",
      "상품과 주문한 상품의 크기가 일치하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}