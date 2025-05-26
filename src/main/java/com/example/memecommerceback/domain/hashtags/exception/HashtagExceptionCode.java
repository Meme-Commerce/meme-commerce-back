package com.example.memecommerceback.domain.hashtags.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HashtagExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "HASHTAG-001",
      "찾을 수 없는 해시태그입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
