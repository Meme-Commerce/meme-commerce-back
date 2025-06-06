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
  @Schema(name = "PaymentResponseDto.ConfirmOneDto", description = "결제 하나 요청 응답 DTO")
  public static class ConfirmOneDto {

    @Schema(description = "주문 번호", example = "order-20240607-00001")
    private String orderId;
    @Schema(description = "결제 키", example = "pay_abc123def456")
    private String paymentKey;
    @Schema(description = "승인 일시", example = "2024-06-01T13:33:44")
    private LocalDateTime approvedAt;
    @Schema(description = "결제 금액", example = "15000")
    private Long amount;
    @Schema(description = "결제 수단 (예: CARD, 가상계좌 등)", example = "CARD")
    private String method;
    @Schema(description = "마스킹된 카드 번호", example = "1234-****-****-5678")
    private String markedCardNumber;
    @Schema(description = "결제 상태", example = "DONE")
    private String status;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentResponseDto.ReadOneDto", description = "결제 하나 조회 응답 DTO")
  public static class ReadOneDto {

    @Schema(description = "주문 번호", example = "order-20240607-00001")
    private String orderId;
    @Schema(description = "결제 키", example = "pay_abc123def456")
    private String paymentKey;
    @Schema(description = "결제 상태", example = "DONE")
    private String status;
    @Schema(description = "승인 일시", example = "2024-06-01T13:33:44")
    private LocalDateTime approvedAt;
    @Schema(description = "결제 금액", example = "15000")
    private Long amount;
    @Schema(description = "결제 수단 (예: CARD, 가상계좌 등)", example = "CARD")
    private String method;
    @Schema(description = "마스킹된 카드 번호", example = "1234-****-****-5678")
    private String markedCardNumber;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentResponseDto.CancelOneDto", description = "결제 하나 취소 응답 DTO")
  public static class CancelOneDto {

    @Schema(description = "주문 번호", example = "order-20240607-00001")
    private String orderId;
    @Schema(description = "결제 키", example = "pay_abc123def456")
    private String paymentKey;
    @Schema(description = "결제 상태", example = "CANCELED")
    private String status;
    @Schema(description = "취소 일시", example = "2024-06-01T13:33:44")
    private LocalDateTime approvedAt;
    @Schema(description = "취소 금액", example = "15000")
    private Long amount;
    @Schema(description = "결제 수단 (예: CARD, 가상계좌 등)", example = "CARD")
    private String method;
    @Schema(description = "마스킹된 카드 번호", example = "1234-****-****-5678")
    private String markedCardNumber;
  }
}
