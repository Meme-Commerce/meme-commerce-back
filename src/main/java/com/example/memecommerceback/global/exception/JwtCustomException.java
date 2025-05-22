package com.example.memecommerceback.global.exception;


public class JwtCustomException extends CustomException {

  public JwtCustomException(GlobalExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

