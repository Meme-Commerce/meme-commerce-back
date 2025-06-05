package com.example.memecommerceback.domain.payment.entity;

import com.example.memecommerceback.domain.payment.exception.PaymentCustomException;
import com.example.memecommerceback.domain.payment.exception.PaymentExceptionCode;
import lombok.Getter;

@Getter
public enum PaymentStatus {
  SUCCESS(Status.SUCCESS), CANCELED(Status.CANCELED), FAIL(Status.FAIL);

  private final String status;

  PaymentStatus(String status) {
    this.status = status;
  }

  public class Status {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String CANCELED = "CANCELED";
  }

  public static PaymentStatus fromStatus(String status) {
    for (PaymentStatus ps : PaymentStatus.values()) {
      if (ps.getStatus().equals(status)) {
        return ps;
      }
    }
    throw new PaymentCustomException(PaymentExceptionCode.UNKNOWN_STATUS);
  }
}
