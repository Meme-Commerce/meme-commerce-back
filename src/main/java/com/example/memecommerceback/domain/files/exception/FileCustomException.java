package com.example.memecommerceback.domain.files.exception;

import com.example.memecommerceback.global.exception.CustomException;

public class FileCustomException extends CustomException {

  public FileCustomException(FileExceptionCode e) {
    super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
  }
}

