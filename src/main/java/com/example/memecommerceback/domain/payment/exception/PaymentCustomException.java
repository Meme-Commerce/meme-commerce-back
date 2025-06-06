package com.example.memecommerceback.domain.payment.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class PaymentCustomException extends CustomException {

  public PaymentCustomException(PaymentExceptionCode e, String errorMessage) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage() + errorMessage);
  }

  public PaymentCustomException(PaymentExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}
