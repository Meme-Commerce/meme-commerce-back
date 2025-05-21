package com.example.memecommerceback.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto<T> {

  private T data;
  private String message;
  private Integer httpStatusCode;

  public CommonResponseDto(T data, String message) {
    this.data = data;
    this.message = message;
  }
}

