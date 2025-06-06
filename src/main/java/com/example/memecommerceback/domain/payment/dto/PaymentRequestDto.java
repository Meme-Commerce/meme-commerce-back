package com.example.memecommerceback.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentRequestDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentRequestDto.ConfirmOneDto",
      description = "결제 승인 요청 DTO")
  public static class ConfirmOneDto {

    @Schema(description = "주문 ID", example = "0e8a4b57-8ad6-4c64-8e57-086c7b6778fa")
    @NotNull(message = "주문 아이디는 필수 입력란입니다.")
    private UUID orderId;
    @Schema(description = "총 결제 금액(최소 100원)", example = "12500")
    @NotNull(message = "총 결제 금액은 필수 입력란입니다.")
    @Min(value = 100, message = "총 결제 금액의 최소 값은 100이상입니다.")
    private Long amount;
    @Schema(description = "토스 결제 키(paymentKey)", example = "pay_123456789012345678901234")
    @NotNull(message = "페이먼츠 키는 필수 입력한입니다.")
    private String paymentKey;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentRequestDto.CancelOneDto",
      description = "결제 취소 요청 DTO")
  public static class CancelOneDto {

    @Schema(description = "토스 결제 키(paymentKey)", example = "pay_123456789012345678901234")
    @NotNull(message = "페이먼츠 키는 필수 입력란입니다.")
    private String paymentKey;
    @Schema(description = "결제 취소 사유", example = "단순변심")
    @NotNull(message = "결제 취소 사유는 필수 입력란입니다.")
    private String cancelReason;
    @Schema(description = "취소 금액", example = "10000")
    private int cancelAmount;
    @Schema(description = "취소 요청 ID(선택)", example = "cancel_req_001")
    private String cancelRequestId;
    @Schema(description = "통화 단위", example = "KRW")
    private String currency;
    @Schema(description = "분할 결제 여부", example = "false")
    private boolean dividedPayment;
    @Schema(description = "환불 계좌 정보(실 계좌 환불 시)", implementation = RefundReceiveAccount.class)
    private RefundReceiveAccount refundReceiveAccount;
    @Schema(description = "과세 금액", example = "9100")
    private long taxAmount;
    @Schema(description = "비과세 금액", example = "0")
    private int taxExemptionAmount;
    @Schema(description = "면세 금액", example = "0")
    private long taxFreeAmount;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "RefundReceiveAccount", description = "환불 받을 계좌 정보")
    public static class RefundReceiveAccount {

      @Schema(description = "계좌 번호", example = "110123456789")
      private String accountNumber;
      @Schema(description = "은행명(코드)", example = "KB")
      private String bank;
      @Schema(description = "예금주명", example = "홍길동")
      private String holderName;
    }
  }
}
