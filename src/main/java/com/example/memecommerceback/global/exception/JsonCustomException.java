package com.example.memecommerceback.global.exception;

public class JsonCustomException extends CustomException {

  public JsonCustomException(GlobalExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

