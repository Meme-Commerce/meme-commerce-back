package com.example.memecommerceback.domain.user.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class UsersCustomException extends CustomException {

  public UsersCustomException(UsersExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}
