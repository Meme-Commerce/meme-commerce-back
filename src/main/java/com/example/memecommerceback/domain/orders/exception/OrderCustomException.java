package com.example.memecommerceback.domain.orders.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class OrderCustomException extends CustomException {

  public OrderCustomException(OrderExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }

  public OrderCustomException(OrderExceptionCode e, String customMessage) {
    super(e.getHttpStatus(), e.getErrorCode(), customMessage);
  }
}
