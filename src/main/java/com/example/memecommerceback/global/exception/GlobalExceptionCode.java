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
      HttpStatus.UNAUTHORIZED, "JWT-001",
      "요청에 JWT token이 포함되지 않습니다."),
  EXPIRED_TOKEN(
      HttpStatus.UNAUTHORIZED, "JWT-002",
      "모든 JWT Token이 만료 되었습니다."),
  INVALID_TOKEN_VALUE(
      HttpStatus.UNAUTHORIZED, "JWT-003",
      "요청하신 토큰은 JWT Token이 아닙니다."),
  NOT_EXIST_REFRESH_TOKEN(
      HttpStatus.UNAUTHORIZED, "JWT-004",
      "요청하신 Refresh Token은 서버에 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
