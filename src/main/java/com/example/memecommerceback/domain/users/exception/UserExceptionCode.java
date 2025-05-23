package com.example.memecommerceback.domain.users.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionCode {

  NOT_FOUND(
      HttpStatus.BAD_REQUEST, "USER-001",
      "해당 유저를 찾을 수 없습니다."),
  NOT_EXIST_AUTHORITY(
      HttpStatus.BAD_REQUEST, "USER-002",
      "해당 권한이 존재하지 않습니다."),
  NEED_TO_REGISTER_NICKNAME(
      HttpStatus.BAD_REQUEST, "USER-003",
      "먼저 닉네임을 설정하셔야 이용할 수 있습니다."
      + "닉네임만 먼저 설정해주세요"),
  REQUEST_DUPLICATE_NICKNAME(
      HttpStatus.BAD_REQUEST, "USER-004",
      "요청하신 닉네임은 중복 닉네임입니다. "),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
