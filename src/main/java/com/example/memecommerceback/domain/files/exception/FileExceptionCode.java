package com.example.memecommerceback.domain.files.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "FILE-001",
      "찾을 수 없는 파일입니다."),
  NOT_SUPPORTED_EXTENSION(
      HttpStatus.BAD_REQUEST, "FILE-002",
      "지원하지 않는 확장자입니다."),
  NICKNAME_REQUIRED_FOR_PROFILE_UPLOAD(
      HttpStatus.BAD_REQUEST, "FILE-003",
      "파일을 업로드 하기 전, 닉네임을 정하셔야 합니다."),
  FILE_IS_REQUIRED(
      HttpStatus.BAD_REQUEST, "FILE-004",
      "등록을 위해 파일이 필요합니다."),
  NOT_REGISTER_OVER_MAX_PRODUCT_IMAGES(
      HttpStatus.BAD_REQUEST, "FILE-005",
      "상품 파일 등록은 최대 5개까지입니다."),
  FAILED_DELETE_IMAGE_S3(
      HttpStatus.BAD_REQUEST, "FILE-006",
      "S3 버킷에서 이미지 파일을 삭제하는데, 실패하였습니다"),
  COUNT_OUT_OF_RANGE(
      HttpStatus.BAD_REQUEST, "FILE-007",
      "판매자 등록 파일은 최소 4개, 최대 10개까지 등록 가능합니다."),
  EMPTY_FILE_LIST(
      HttpStatus.BAD_REQUEST, "FILE-008",
      "요청에 대한 파일 리스트는 비어있으면 안됩니다."),
  EMPTY_FILE(
      HttpStatus.BAD_REQUEST, "FILE-009",
      "요청에 대한 파일은 비어있으면 안됩니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
