package com.example.memecommerceback.domain.payment.service;


import com.example.memecommerceback.domain.payment.dto.PaymentRequestDto;
import com.example.memecommerceback.domain.payment.dto.PaymentResponseDto;
import com.example.memecommerceback.domain.payment.entity.Payment;
import com.example.memecommerceback.domain.payment.exception.PaymentCustomException;
import com.example.memecommerceback.domain.payment.exception.PaymentExceptionCode;

public interface PaymentServiceV1 {

  /**
   * 결제 승인 요청
   *
   * @param requestDto {@link PaymentRequestDto.ConfirmOneDto} 결제 승인 요청 DTO
   * @return {@link PaymentResponseDto.ConfirmOneDto} 결제 승인 응답 DTO
   */
  PaymentResponseDto.ConfirmOneDto confirmOne(
      PaymentRequestDto.ConfirmOneDto requestDto);

  /**
   * 결제 하나 조회
   *
   * @param paymentKey : 토스에서 발급해준 PaymentKey
   * @return {@link PaymentResponseDto.ReadOneDto} 결제 조회 응답 DTO
   * @throws PaymentCustomException {@link PaymentExceptionCode#NOT_FOUND} 토스페이먼츠에서 발급해준 paymentKey와
   *                                Payment 데이터와 일치하는 것이 없을 때
   */
  PaymentResponseDto.ReadOneDto readOne(String paymentKey);

  /**
   * 결제 하나 취소 (부분 취소 안됨.)
   *
   * @param requestDto {@link PaymentRequestDto.CancelOneDto} 결제 하나 취소 요청 DTO
   * @return {@link PaymentResponseDto.CancelOneDto} 결제 하나 취소 응답 DTO
   * @throws PaymentCustomException {@link PaymentExceptionCode#NOT_FOUND} requestDto에서 받은 토스페이먼츠에서
   *                                발급해준 paymentKey와 Payment 데이터와 일치하는 것이 없을 때
   * @throws PaymentCustomException {@link PaymentExceptionCode#ALREADY_CANCELED_PAYMENT} 이미 취소한 결제를
   *                                추가로 취소하려고 할 때
   */
  PaymentResponseDto.CancelOneDto cancelOne(
      PaymentRequestDto.CancelOneDto requestDto);

  /**
   * paymentKey를 통한 결제 하나 조회
   *
   * @param paymentKey : 토스에서 발급해준 paymentKey
   * @return paymentKey와 일치하는 Payment
   * @throws PaymentCustomException {@link PaymentExceptionCode#NOT_FOUND} 토스페이먼츠에서 발급해준 paymentKey와
   *                                Payment 데이터와 일치하는 것이 없을 때
   */
  Payment findByPaymentKey(String paymentKey);
}
