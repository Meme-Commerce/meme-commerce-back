package com.example.memecommerceback.domain.payment.service;

import com.example.memecommerceback.domain.payment.dto.PaymentRequestDto;
import com.example.memecommerceback.domain.payment.dto.TossPaymentResponseDto;
import com.example.memecommerceback.domain.payment.exception.PaymentCustomException;
import com.example.memecommerceback.domain.payment.exception.PaymentExceptionCode;

public interface TossPaymentServiceV1 {


  /**
   * 토스 페이먼츠에서 제공하는 결제 하나 승인
   *
   * @param requestDto {@link PaymentRequestDto.ConfirmOneDto} 결제 승인 요청 DTO
   * @return {@link TossPaymentResponseDto.ConfirmOneDto} 토스 페이먼츠에서 제공하는 결제 하나 승인 응답 DTO
   * @throws PaymentCustomException {@link PaymentExceptionCode#TOSS_PAYMENT_REQUEST_FAIL} 토스 페이먼츠
   *                                응답 실패로 인한 결제 요청 실패
   */
  TossPaymentResponseDto.ConfirmOneDto confirmOne(
      PaymentRequestDto.ConfirmOneDto requestDto);

  /**
   * 토스 페이먼츠에서 제공하는 결제 하나 조회
   *
   * @param paymentKey : 토스페이먼츠에서 발급한 paymentKey
   * @return {@link TossPaymentResponseDto.ReadOneDto} 토스 페이먼츠에서 제공하는 결제 하나 조회 응답 DTO
   * @throws PaymentCustomException {@link PaymentExceptionCode#TOSS_PAYMENT_REQUEST_FAIL} 토스 페이먼츠
   *                                응답 실패로 인한 결제 조회 요청 실패
   */
  TossPaymentResponseDto.ReadOneDto readOne(String paymentKey);

  /**
   * 토스 페이먼츠에서 제공하는 결제 하나 취소
   *
   * @param requestDto {@link PaymentRequestDto.CancelOneDto} 결제 취소 요청 DTO
   * @return {@link TossPaymentResponseDto.CancelOneDto} 토스 페이먼츠에서 제공하는 결제 취소 응답 DTO
   * @throws PaymentCustomException {@link PaymentExceptionCode#TOSS_PAYMENT_CANCEL_FAIL} 토스 페이먼츠 응답
   *                                실패로 인한 결제 하나 취소 요청 실패
   */
  TossPaymentResponseDto.CancelOneDto cancelOne(
      PaymentRequestDto.CancelOneDto requestDto);
}
