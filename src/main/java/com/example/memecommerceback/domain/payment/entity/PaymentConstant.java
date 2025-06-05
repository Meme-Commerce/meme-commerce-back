package com.example.memecommerceback.domain.payment.entity;

public class PaymentConstant {

  public static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
  public static final String TOSS_READ_URL = "https://api.tosspayments.com/v1/payments/{paymentKey}";
  public static final String CANCEL_URL = "https://api.tosspayments.com/v1/payments/";
  public static final String CANCEL = "/cancel";
  public static final String BASIC = "Basic ";
  public static final String PAYMENT_KEY = "paymentKey";
  public static final String ORDER_ID = "orderId";
  public static final String AMOUNT = "amount";
  public static final String CANCEL_REASON = "cancelReason";
  // 멱등 키
  public static final String IDEMPOTENCY_KEY = "Idempotency-Key";
  public static final String DONE = "DONE";
}
