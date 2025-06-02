package com.example.memecommerceback.domain.meme.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class MemeCustomException extends CustomException {

  public MemeCustomException(MemeExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }

  public MemeCustomException(MemeExceptionCode e, String customMessage) {
    super(e.getHttpStatus(), e.getErrorCode(), customMessage);
  }
}
