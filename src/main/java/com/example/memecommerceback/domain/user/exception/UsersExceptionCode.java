package com.example.memecommerceback.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UsersExceptionCode {

  NOT_FOUND(
      HttpStatus.BAD_REQUEST, "USERS-001",
      "해당 유저를 찾을 수 없습니다."),
  NOT_EXIST_AUTHORITY(
      HttpStatus.BAD_REQUEST, "USERS-002",
      "해당 권한이 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
