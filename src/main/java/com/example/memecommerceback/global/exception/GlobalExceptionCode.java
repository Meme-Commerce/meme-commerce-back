package com.example.memecommerceback.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {

        ,;

        private final HttpStatus httpStatus;
        private final String errorCode;
        private final String message;
}
