package com.example.memecommerceback.domain.users.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class UserCustomException extends CustomException {

  public UserCustomException(UserExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}
