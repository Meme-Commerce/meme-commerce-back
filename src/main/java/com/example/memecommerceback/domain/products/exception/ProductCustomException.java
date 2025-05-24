package com.example.memecommerceback.domain.products.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class ProductCustomException extends CustomException {

  public ProductCustomException(ProductExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}
