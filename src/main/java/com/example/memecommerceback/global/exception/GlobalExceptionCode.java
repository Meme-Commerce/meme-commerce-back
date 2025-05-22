package com.example.memecommerceback.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {

  // PROFANITY-ERROR
  NOT_BLANK(
      HttpStatus.BAD_REQUEST, "PROFANITY-001",
      "해당 텍스트는 공란입니다."),
  PROFANITY_DETECTED(
      HttpStatus.BAD_REQUEST, "PROFANITY-002",
      "해당 텍스트에 비속어가 들어가 있습니다."),

  // JWT-ERROR
  MISSING_TOKEN(
      HttpStatus.BAD_REQUEST, "JWT-001",
      "요청에 JWT token이 포함되지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
