package com.example.memecommerceback.global.exception;

public class OAuth2CustomException extends CustomException {

    public OAuth2CustomException(GlobalExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
