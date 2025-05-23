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

  // Json Error
  FAILED_SERIALIZATION(
      HttpStatus.UNAUTHORIZED, "JSON-001",
      "요청하신 Token을 변환하는 데, 실패하였습니다."),

  // OAuth2
  NOT_FOUND_RESPONSE(
      HttpStatus.BAD_GATEWAY, "OAUTH-001",
      "제공자 응답에서 response를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_ID(
      HttpStatus.BAD_GATEWAY, "OAUTH-002",
      "제공자 응답에서 response id 필드를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_NAME(
      HttpStatus.BAD_GATEWAY, "OAUTH-003",
      "제공자 응답에서 response name 필드를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_EMAIL(
      HttpStatus.BAD_GATEWAY, "OAUTH-004",
      "제공자 응답에서 response email 필드를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_GENDER(
      HttpStatus.BAD_GATEWAY, "OAUTH-005",
      "제공자 응답에서 response gender 필드를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_BIRTHDAY(
      HttpStatus.BAD_GATEWAY, "OAUTH-006",
      "제공자 응답에서 response birthday 필드를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_BIRTHYEAR(
      HttpStatus.BAD_GATEWAY, "OAUTH-007",
      "제공자 응답에서 response birthyear 필드를 찾을 수 없습니다."),
  NOT_FOUND_RESPONSE_CONTACT(
      HttpStatus.BAD_GATEWAY, "OAUTH-008",
      "제공자 응답에서 response contact 필드를 찾을 수 없습니다."),
  NOT_FOUND_PROVIDER(
      HttpStatus.BAD_REQUEST, "OAUTH-009",
      "지원하지 않거나 찾을 수 없는 공급자입니다."),

  // S3
  UPLOAD_FAIL(
      HttpStatus.BAD_REQUEST, "AWS-001",
      "S3 버킷에 업로드를 실패하였습니다."),
  BLANK_FILE(
      HttpStatus.BAD_REQUEST, "AWS-002",
      "해당 파일은 공란입니다."),
  NOT_MATCHED_FILE_URL(
      HttpStatus.BAD_REQUEST, "AWS-003",
      "요청하신 URL이 잘못되었습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
