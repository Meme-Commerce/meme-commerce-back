package com.example.memecommerceback.domain.meme.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemeExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "MEME-001",
      "해당 밈을 찾을 수 없습니다."),
  SIMILAR_NAME(
      HttpStatus.BAD_REQUEST, "MEME-002",
      "제목이 유사한 밈이 존재합니다."),
  SIMILAR_DESCRIPTION(
      HttpStatus.BAD_REQUEST, "MEME-003",
      "설명이 유사한 밈이 존재합니다."),
  MAX_CREATE_MEME_LIMIT_EXCEEDED(
      HttpStatus.BAD_REQUEST, "MEME-004",
      "밈 최대 생성 갯수는 10개입니다."),
  CANNOT_MODIFY_STATUS(
      HttpStatus.BAD_REQUEST, "MEME-005",
      "밈이 REJECTED, APPROVE 상태이면 밈을 수정할 수 없습니다. "),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}