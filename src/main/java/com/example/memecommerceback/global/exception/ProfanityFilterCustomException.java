package com.example.memecommerceback.global.exception;

public class ProfanityFilterCustomException extends CustomException {

  public ProfanityFilterCustomException(GlobalExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}
