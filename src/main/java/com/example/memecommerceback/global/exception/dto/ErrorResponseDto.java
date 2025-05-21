package com.example.memecommerceback.global.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "에러 응답 DTO")
public class ErrorResponseDto {

  @Schema(description = "에러 코드", example = "JWT-002")
  private String errorCode;

  @Schema(description = "에러 메시지", example = "요청에 JWT token이 포함되지 않습니다.")
  private String message;
}
