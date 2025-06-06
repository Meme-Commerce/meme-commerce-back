package com.example.memecommerceback.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentResponseDto.ConfirmOneDto",
      description = "결제 하나 요청 응답 DTO")
  public static class ConfirmOneDto {

    private String orderId;
    private String paymentKey;
    private LocalDateTime approvedAt;
    private Long amount;
    private String method;
    private String markedCardNumber;
    private String status;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentResponseDto.ReadOneDto",
      description = "결제 하나 조회 응답 DTO")
  public static class ReadOneDto {

    private String orderId;
    private String paymentKey;
    private String status;
    private LocalDateTime approvedAt;
    private Long amount;
    private String method;
    private String markedCardNumber;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentResponseDto.CancelOneDto",
      description = "결제 하나 취소 응답 DTO")
  public static class CancelOneDto {

    private String orderId;
    private String paymentKey;
    private String status;
    private LocalDateTime approvedAt;
    private Long amount;
    private String method;
    private String markedCardNumber;
  }
}
