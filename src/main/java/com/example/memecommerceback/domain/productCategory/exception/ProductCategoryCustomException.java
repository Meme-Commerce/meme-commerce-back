package com.example.memecommerceback.domain.productCategory.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class ProductCategoryCustomException extends CustomException {

  public ProductCategoryCustomException(ProductCategoryExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

