package com.example.memecommerceback.domain.payment.service;

import com.example.memecommerceback.domain.payment.dto.PaymentRequestDto;
import com.example.memecommerceback.domain.payment.dto.TossPaymentResponseDto;
import com.example.memecommerceback.domain.payment.entity.PaymentConstant;
import com.example.memecommerceback.domain.payment.exception.PaymentCustomException;
import com.example.memecommerceback.domain.payment.exception.PaymentExceptionCode;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TossPaymentServiceImplV1 implements TossPaymentServiceV1 {

  @Value("${toss.secret.key}")
  private String tossTestSecretKey;

  private final RestTemplate restTemplate;

  @Override
  @Transactional
  public TossPaymentResponseDto.ConfirmOneDto confirmOne(
      PaymentRequestDto.ConfirmOneDto request) {
    Map<String, Object> body = new HashMap<>();
    body.put(PaymentConstant.PAYMENT_KEY, request.getPaymentKey());
    body.put(PaymentConstant.ORDER_ID, request.getOrderId());
    body.put(PaymentConstant.AMOUNT, request.getAmount());

    HttpEntity<Map<String, Object>> entity = createEntity(body);

    try {
      ResponseEntity<TossPaymentResponseDto.ConfirmOneDto> resp =
          restTemplate.postForEntity(
              PaymentConstant.TOSS_CONFIRM_URL, entity,
              TossPaymentResponseDto.ConfirmOneDto.class);
      return resp.getBody();
    } catch (Exception e) {
      throw new PaymentCustomException(
          PaymentExceptionCode.TOSS_PAYMENT_REQUEST_FAIL, e.getMessage());
    }
  }

  @Override
  @Transactional
  public TossPaymentResponseDto.ReadOneDto readOne(String paymentKey) {
    HttpEntity<Void> entity = createEntity();

    try {
      ResponseEntity<TossPaymentResponseDto.ReadOneDto> resp =
          restTemplate.exchange(PaymentConstant.TOSS_READ_URL,
              HttpMethod.GET, entity,
              TossPaymentResponseDto.ReadOneDto.class, paymentKey);
      return resp.getBody();
    } catch (Exception e) {
      throw new PaymentCustomException(
          PaymentExceptionCode.TOSS_PAYMENT_REQUEST_FAIL, e.getMessage());
    }
  }

  @Override
  @Transactional
  public TossPaymentResponseDto.CancelOneDto cancelOne(PaymentRequestDto.CancelOneDto requestDto) {
    Map<String, Object> body = new HashMap<>();
    body.put(PaymentConstant.CANCEL_REASON, requestDto.getCancelReason());

    HttpHeaders headers = buildHeaders();
    headers.set(PaymentConstant.IDEMPOTENCY_KEY, UUID.randomUUID().toString());

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<TossPaymentResponseDto.CancelOneDto> response =
          restTemplate.postForEntity(
              PaymentConstant.CANCEL_URL + requestDto.getPaymentKey()
                  + PaymentConstant.CANCEL,
              entity, TossPaymentResponseDto.CancelOneDto.class);

      return response.getBody();
    } catch (Exception e) {
      throw new PaymentCustomException(
          PaymentExceptionCode.TOSS_PAYMENT_CANCEL_FAIL, e.getMessage());
    }
  }


  private HttpHeaders buildHeaders() {
    String encoded = Base64.getEncoder()
        .encodeToString((tossTestSecretKey + ":").getBytes());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, PaymentConstant.BASIC + encoded);
    return headers;
  }

  private <T> HttpEntity<T> createEntity(T body) {
    return new HttpEntity<>(body, buildHeaders());
  }

  private HttpEntity<Void> createEntity() {
    return new HttpEntity<>(buildHeaders());
  }
}
