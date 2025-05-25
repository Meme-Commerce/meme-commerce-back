package com.example.memecommerceback.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {

  // PROFANITY ERROR
  NOT_BLANK(
      HttpStatus.BAD_REQUEST, "PROFANITY-001",
      "해당 텍스트는 공란입니다."),
  PROFANITY_DETECTED(
      HttpStatus.BAD_REQUEST, "PROFANITY-002",
      "해당 텍스트에 비속어가 들어가 있습니다."),

  // JWT ERROR
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

  // OAuth2 ERROR
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
      HttpStatus.NOT_FOUND, "OAUTH-009",
      "지원하지 않거나 찾을 수 없는 공급자입니다."),

  // AWS S3 ERROR
  UPLOAD_FAIL(
      HttpStatus.BAD_REQUEST, "AWS-001",
      "S3 버킷에 업로드를 실패하였습니다."),
  BLANK_FILE(
      HttpStatus.BAD_REQUEST, "AWS-002",
      "해당 파일은 공란입니다."),
  NOT_MATCHED_FILE_URL(
      HttpStatus.BAD_REQUEST, "AWS-003",
      "요청하신 URL이 잘못되었습니다."),
  ALREADY_EXISTS_FILE_URL(
      HttpStatus.BAD_REQUEST, "AWS-004",
      "요청하신 닉네임과 중복되는 파일 경로가 존재합니다."
          + " 다른 닉네임으로 저장해주시길 바랍니다."),

  // DATE-ERROR
  DATE_BEFORE_TODAY(
      HttpStatus.BAD_REQUEST, "DATE-TIME-001",
      "요청하신 날짜가 오늘 이전일 수 없습니다."),
  END_DATE_BEFORE_START_DATE(
      HttpStatus.BAD_REQUEST, "DATE-TIME-002",
      "요청하신 시작일이 마감일보다 빠를 수 없습니다."),
  REQUEST_SEVEN_DAYS(
      HttpStatus.BAD_REQUEST, "DATE-TIME-003",
      "핫링크 요청 일은 7일만 가능합니다."),
  DATE_TOO_FAR(
      HttpStatus.BAD_REQUEST, "DATE-TIME-004",
      "핫링크 요청은 현 시간으로부터 3달까지만 허용됩니다."),

  // PAGE-ERROR
  INVALID_SORT_FIELDS(
      HttpStatus.BAD_REQUEST, "PAGE-001",
      "는 정렬할 수 없는 필드입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
