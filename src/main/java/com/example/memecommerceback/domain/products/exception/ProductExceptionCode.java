package com.example.memecommerceback.domain.products.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "PRODUCT-001",
      "해당 상품을 찾을 수 없습니다."),
  UNKNOWN_STATUS(
      HttpStatus.BAD_REQUEST, "PRODUCT-002",
      "서버에 존재하지 않는 상품 상태입니다."),
  SIMILAR_PRODUCT_TITLE_EXISTS(
      HttpStatus.BAD_REQUEST, "PRODUCT-003",
      "상품명이 기존 등록 상품과 너무 유사합니다."),
  SIMILAR_PRODUCT_DESCRIPTION_EXISTS(
      HttpStatus.BAD_REQUEST, "PRODUCT-004",
      "상품 설명이 기존 등록 상품과 너무 유사합니다."),
  REQUEST_SAME_STATUS(
      HttpStatus.BAD_REQUEST, "PRODUCT-005",
      "상품 상태와 동일한 요청을 하셨습니다. 다시 요청해주세요."),
  CANNOT_MODIFY_STATUS(
      HttpStatus.BAD_REQUEST, "PRODUCT-006",
      "변경할 수 없는 상태로 요청하였습니다."),
  NOT_OWNER(
      HttpStatus.FORBIDDEN, "PRODUCT-007",
      "상품의 주인이 아닙니다."),
  NEED_TO_SELL_DATE(
      HttpStatus.BAD_REQUEST, "PRODUCT-008",
      "상품을 판매 또는 재판매 할 경우, 다시 판매 시작일, 마감일을 적으셔야합니다."),
  UNAUTHORIZED_READ(
      HttpStatus.FORBIDDEN, "PRODUCT-009",
      "해당 유저는 읽을 권한이 없습니다."),
  UNAUTHORIZED_DELETE(
      HttpStatus.FORBIDDEN, "PRODUCT-010",
      "해당 유저는 삭제할 권한이 없습니다."
  ),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}