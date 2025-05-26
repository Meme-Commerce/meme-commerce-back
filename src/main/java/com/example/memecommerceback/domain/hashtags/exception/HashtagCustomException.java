package com.example.memecommerceback.domain.hashtags.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class HashtagCustomException extends CustomException {

  public HashtagCustomException(HashtagExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }

  public HashtagCustomException(HashtagExceptionCode e, String customMessage) {
    super(e.getHttpStatus(), e.getErrorCode(), customMessage);
  }
}

