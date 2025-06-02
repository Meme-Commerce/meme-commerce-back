package com.example.memecommerceback.domain.memeEmoji.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemeEmojiExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "MEME-EMOJI-001",
      "해당 밈모지를 찾을 수 없습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}