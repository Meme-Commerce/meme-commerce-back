package com.example.memecommerceback.global.exception;

public class DateCustomException extends CustomException {

    public DateCustomException(GlobalExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
