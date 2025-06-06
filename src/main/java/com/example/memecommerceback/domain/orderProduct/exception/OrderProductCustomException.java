package com.example.memecommerceback.domain.orderProduct.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class OrderProductCustomException extends CustomException {

  public OrderProductCustomException(OrderProductExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

