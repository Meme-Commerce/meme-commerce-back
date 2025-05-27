package com.example.memecommerceback.global.exception;

public class PageCustomException extends CustomException {

  public PageCustomException(GlobalExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }

  public PageCustomException(GlobalExceptionCode e, String message) {
    super(e.getHttpStatus(), e.getErrorCode(), message + e.getMessage());
  }
}

