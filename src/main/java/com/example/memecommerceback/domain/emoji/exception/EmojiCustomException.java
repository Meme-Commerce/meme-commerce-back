package com.example.memecommerceback.domain.emoji.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class EmojiCustomException extends CustomException {

  public EmojiCustomException(EmojiExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

