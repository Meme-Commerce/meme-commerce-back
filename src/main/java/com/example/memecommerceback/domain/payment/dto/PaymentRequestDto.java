package com.example.memecommerceback.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "주문 아이디는 필수 입력란입니다.")
    private UUID orderId;
    @NotNull(message = "총 결제 금액은 필수 입력란입니다.")
    @Min(value = 100, message = "총 결제 금액의 최소 값은 100이상입니다.")
    private Long amount;
    @NotNull(message = "페이먼츠 키는 필수 입력한입니다.")
    private String paymentKey;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "PaymentRequestDto.CancelOneDto",
      description = "결제 승인 요청 DTO")
  public static class CancelOneDto {

    @NotNull(message = "페이먼츠 키는 필수 입력란입니다.")
    private String paymentKey;
    @NotNull(message = "결제 취소 사유는 필수 입력란입니다.")
    private String cancelReason;
    private int cancelAmount;
    private String cancelRequestId;
    private String currency;
    private boolean dividedPayment;
    private RefundReceiveAccount refundReceiveAccount;
    private long taxAmount;
    private int taxExemptionAmount;
    private long taxFreeAmount;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefundReceiveAccount {

      private String accountNumber;
      private String bank;
      private String holderName;
    }
  }
}
