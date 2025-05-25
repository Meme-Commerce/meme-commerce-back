package com.example.memecommerceback.domain.categories.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class CategoryCustomException extends CustomException {

  public CategoryCustomException(CategoryExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

