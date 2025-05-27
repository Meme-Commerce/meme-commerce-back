package com.example.memecommerceback.global.exception.dto;

import lombok.Getter;

@Getter
public enum Error {

  INVALID_PARAMETER_ERROR(
      Code.INVALID_PARAMETER_ERROR,
      Message.INVALID_PARAMETER_ERROR),
  INVALID_DTO_MAPPING_ERROR(
      Code.INVALID_DTO_MAPPING_ERROR,
      Message.INVALID_DTO_MAPPING_ERROR),
  JWT_AUTHENTICATION_ERROR(
      Message.JWT_AUTHENTICATION_ERROR),
  AUTH_DENIED_ERROR(
      Code.AUTH_ERROR,
      Message.ACCESS_DENIED),
  MISSING_REQUEST_PART(
      Code.REQUEST_PART_ERROR,
      Message.MISSING_REQUEST_PART),
  DATE_TIME_PARSE_ERROR(
      Code.DATE_TIME_PARSE_ERROR,
      Message.DATE_TIME_PARSE_ERROR),
  INVALID_ENUM_VALUE(
      Code.INPUT_INVALID_ENUM,
      Message.INPUT_INVALID_ENUM),
  UPLOAD_EXCEED_FILE_SIZE(
      Code.UPLOAD_EXCEED_FILE_SIZE,
      Message.UPLOAD_EXCEED_FILE_SIZE),
  NOT_FOUND_USER(
      Code.NOT_FOUND_USER,
      Message.NOT_FOUND_USER),
  LOGOUT_ERROR(
      Code.LOGOUT_ERROR,
      Message.LOGOUT_ERROR),
  ;

  private final String code;
  private final String message;

  Error(String message) {
    this.code = null;
    this.message = message;
  }

  Error(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static class Code {

    public static final String INPUT_INVALID_ENUM = "INPUT-INVALID-ENUM-ERROR";
    public static final String INVALID_PARAMETER_ERROR = "INVALID-PARAMETER-ERROR";
    public static final String INVALID_DTO_MAPPING_ERROR = "INVALID-DTO-MAPPING-ERROR";
    public static final String AUTH_ERROR = "AUTH-ERROR";
    public static final String REQUEST_PART_ERROR = "REQUEST-PART-ERROR";
    public static final String DATE_TIME_PARSE_ERROR = "DATE-TIME-PARSE-ERROR";
    public static final String UPLOAD_EXCEED_FILE_SIZE = "EXCEED-FILE-SIZE-ERROR";
    public static final String NOT_FOUND_USER = "NOT-FOUND-USER-ERROR";
    public static final String LOGOUT_ERROR = "LOGOUT-ERROR";
  }

  public static class Message {

    public static final String INVALID_PARAMETER_ERROR = "요청 파라미터를 확인해주세요.";
    public static final String INVALID_DTO_MAPPING_ERROR = "DTO 검증을 확인해주세요.";
    public static final String JWT_AUTHENTICATION_ERROR = "JWT 인증 오류 발생가 발생했습니다.";
    public static final String ACCESS_DENIED = "접근 권한이 없습니다.";
    public static final String MISSING_REQUEST_PART = "요청 파트 누락";
    public static final String DATE_TIME_PARSE_ERROR = "요청한 날짜 형식이 올바르지 않습니다.";
    public static final String INPUT_INVALID_ENUM = "해당 입력값에 알맞는 값이 없습니다.";
    public static final String UPLOAD_EXCEED_FILE_SIZE = "서버에서 허용할 수 있는 용량을 초과하였습니다.";
    public static final String NOT_FOUND_USER = "JWT Token 만료 또는 회원 탈퇴로 인하여 사용자를 찾을 수 없습니다.";
    public static final String LOGOUT_ERROR = "로그 아웃 에러";
  }
}
