package com.example.memecommerceback.domain.categories.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "CATEGORY-001",
      "찾을 수 없는 카테고리입니다."),
  ALREADY_EXIST_NAME(
      HttpStatus.BAD_REQUEST, "CATEGORY-002",
      "이미 존재하는 카테고리 이름입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
