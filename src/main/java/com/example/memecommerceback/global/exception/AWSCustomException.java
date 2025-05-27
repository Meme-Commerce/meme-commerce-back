package com.example.memecommerceback.global.exception;

public class AWSCustomException extends CustomException {

  public AWSCustomException(GlobalExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

