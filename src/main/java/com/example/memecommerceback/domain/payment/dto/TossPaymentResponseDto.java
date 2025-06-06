package com.example.memecommerceback.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TossPaymentResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "TossPaymentResponseDto.ConfirmOneDto", description = "결제 하나 요청 응답 DTO")
  public static class ConfirmOneDto {

    @Schema(description = "주문 번호", example = "order_123456789")
    private String orderId;
    @Schema(description = "결제 키", example = "pay_abcdefghijklmn")
    private String paymentKey;
    @Schema(description = "결제 상태 (DONE이면 성공)", example = "DONE")
    private String status;
    @Schema(description = "결제 승인 시간", example = "2025-06-07T13:00:00")
    private LocalDateTime approvedAt;
    @Schema(description = "결제 금액", example = "11000")
    private Long amount;
    @Schema(description = "결제 수단", example = "카드")
    private String method;
    @JsonProperty("card")
    @Schema(description = "카드 정보 객체")
    private CardInfo cardInfo;

    public boolean isSuccessful() {
      return "DONE".equalsIgnoreCase(this.status);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "TossPaymentResponseDto.ReadOneDto", description = "결제 하나 조회 응답 DTO")
  public static class ReadOneDto {

    @Schema(description = "주문 번호", example = "order_123456789")
    private String orderId;
    @Schema(description = "결제 키", example = "pay_abcdefghijklmn")
    private String paymentKey;
    @Schema(description = "결제 상태 (DONE이면 성공)", example = "DONE")
    private String status;
    @Schema(description = "결제 승인 시간",
        example = "2025-06-07T13:00:00")
    private LocalDateTime approvedAt;
    @Schema(description = "결제 금액", example = "11000")
    private Long amount;
    @Schema(description = "결제 수단", example = "카드")
    private String method;
    @JsonProperty("card")
    @Schema(description = "카드 정보 객체")
    private CardInfo cardInfo;

    public boolean isSuccessful() {
      return "DONE".equalsIgnoreCase(this.status);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "TossPaymentResponseDto.CancelOneDto", description = "결제 하나 취소 응답 DTO")
  public static class CancelOneDto {

    @Schema(description = "주문 번호", example = "order_123456789")
    private String orderId;
    @Schema(description = "결제 키", example = "pay_abcdefghijklmn")
    private String paymentKey;
    @Schema(description = "결제 상태 (DONE이면 성공)", example = "DONE")
    private String status;
    @Schema(description = "결제 승인 시간",
        example = "2025-06-07T13:00:00")
    private LocalDateTime approvedAt;
    @Schema(description = "결제 금액", example = "11000")
    private Long amount;
    @Schema(description = "결제 수단", example = "카드")
    private String method;
    @JsonProperty("card")
    @Schema(description = "카드 정보 객체")
    private CardInfo cardInfo;

    public boolean isSuccessful() {
      return "DONE".equalsIgnoreCase(this.status);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CardInfo {

    @Schema(description = "카드 발급사 코드", example = "HANA")
    private String issuerCode;
    @Schema(description = "카드 매입사 코드", example = "SHINHAN")
    private String acquirerCode;
    @Schema(description = "마스킹된 카드 번호",
        example = "1234-****-****-5678")
    private String number;
    @Schema(description = "할부 개월 수", example = "0")
    private int installmentPlanMonths;
    @Schema(description = "무이자 여부", example = "false")
    private boolean isInterestFree;
    @Schema(description = "승인 번호", example = "00012345")
    private String approveNo;

    @JsonProperty("number")
    public String getMaskedCardNumber() {
      return this.number;
    }
  }
}
