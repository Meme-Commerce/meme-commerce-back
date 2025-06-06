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
  @Schema(name = "TossPaymentResponseDto.ConfirmOneDto",
      description = "결제 하나 요청 응답 DTO")
  public static class ConfirmOneDto {

    private String orderId;
    private String paymentKey;
    private String status;
    private LocalDateTime approvedAt;
    private Long amount;
    private String method;
    @JsonProperty("card")
    private CardInfo cardInfo;

    public boolean isSuccessful() {
      return "DONE".equalsIgnoreCase(this.status);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "TossPaymentResponseDto.ReadOneDto",
      description = "결제 하나 조회 응답 DTO")
  public static class ReadOneDto {

    private String orderId;
    private String paymentKey;
    private String status;
    private LocalDateTime approvedAt;
    private Long amount;
    private String method;
    @JsonProperty("card")
    private CardInfo cardInfo;

    public boolean isSuccessful() {
      return "DONE".equalsIgnoreCase(this.status);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "TossPaymentResponseDto.CancelOneDto",
      description = "결제 하나 취소 응답 DTO")
  public static class CancelOneDto {

    private String orderId;
    private String paymentKey;
    private String status;
    private LocalDateTime approvedAt;
    private Long amount;
    private String method;
    @JsonProperty("card")
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

    private String issuerCode;
    private String acquirerCode;
    private String number;  // 마스킹된 카드 번호
    private int installmentPlanMonths;
    private boolean isInterestFree;
    private String approveNo;

    @JsonProperty("number")
    public String getMaskedCardNumber() {
      return this.number;
    }
  }
}
