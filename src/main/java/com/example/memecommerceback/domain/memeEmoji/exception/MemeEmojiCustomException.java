package com.example.memecommerceback.domain.memeEmoji.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class MemeEmojiCustomException extends CustomException {

  public MemeEmojiCustomException(MemeEmojiExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

