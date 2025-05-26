package com.example.memecommerceback.domain.productCategory.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductCategoryExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "PRODUCT-CATEGORY-001",
      "찾을 수 없는 상품 카테고리입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
