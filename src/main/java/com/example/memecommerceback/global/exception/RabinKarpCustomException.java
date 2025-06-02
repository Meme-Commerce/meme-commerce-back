package com.example.memecommerceback.global.exception;

public class RabinKarpCustomException extends CustomException {

  public RabinKarpCustomException(GlobalExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}
