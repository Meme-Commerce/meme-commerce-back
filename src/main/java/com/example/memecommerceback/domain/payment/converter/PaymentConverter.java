package com.example.memecommerceback.domain.payment.converter;


import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.payment.dto.PaymentResponseDto;
import com.example.memecommerceback.domain.payment.dto.TossPaymentResponseDto;
import com.example.memecommerceback.domain.payment.entity.Payment;
import com.example.memecommerceback.domain.payment.entity.PaymentStatus;

public class PaymentConverter {

  public static Payment toEntity(
      TossPaymentResponseDto.ConfirmOneDto tossResponse, Order order,
      PaymentStatus status) {
    return Payment.builder()
        .paymentKey(tossResponse.getPaymentKey())
        .order(order)
        .amount((long) tossResponse.getAmount())
        .approvedAt(tossResponse.getApprovedAt())
        .status(status)
        .build();
  }

  public static PaymentResponseDto.ConfirmOneDto toConfirmOneDto(
      TossPaymentResponseDto.ConfirmOneDto tossResponseDto,
      PaymentStatus status) {
    return PaymentResponseDto.ConfirmOneDto.builder()
        .markedCardNumber(tossResponseDto.getCardInfo().getMaskedCardNumber())
        .method(tossResponseDto.getMethod())
        .amount(tossResponseDto.getAmount())
        .orderId(tossResponseDto.getOrderId())
        .paymentKey(tossResponseDto.getPaymentKey())
        .approvedAt(tossResponseDto.getApprovedAt())
        .status(status.getStatus())
        .build();
  }

  public static PaymentResponseDto.ReadOneDto toReadOneDto(
      TossPaymentResponseDto.ReadOneDto tossResponseDto, PaymentStatus status) {
    return PaymentResponseDto.ReadOneDto.builder()
        .markedCardNumber(tossResponseDto.getCardInfo().getMaskedCardNumber())
        .method(tossResponseDto.getMethod())
        .amount(tossResponseDto.getAmount())
        .orderId(tossResponseDto.getOrderId())
        .paymentKey(tossResponseDto.getPaymentKey())
        .approvedAt(tossResponseDto.getApprovedAt())
        .status(status.getStatus())
        .build();
  }

  public static PaymentResponseDto.CancelOneDto toCancelOneDto(
      TossPaymentResponseDto.CancelOneDto tossResponseDto) {
    return PaymentResponseDto.CancelOneDto.builder()
        .markedCardNumber(tossResponseDto.getCardInfo().getMaskedCardNumber())
        .method(tossResponseDto.getMethod())
        .amount(tossResponseDto.getAmount())
        .orderId(tossResponseDto.getOrderId())
        .paymentKey(tossResponseDto.getPaymentKey())
        .approvedAt(tossResponseDto.getApprovedAt())
        .build();
  }
}
