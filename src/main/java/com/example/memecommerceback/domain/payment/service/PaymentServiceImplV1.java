package com.example.memecommerceback.domain.payment.service;

import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.orderProduct.service.OrderProductServiceV1;
import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.orders.entity.OrderStatus;
import com.example.memecommerceback.domain.orders.service.OrderServiceV1;
import com.example.memecommerceback.domain.payment.converter.PaymentConverter;
import com.example.memecommerceback.domain.payment.dto.PaymentRequestDto;
import com.example.memecommerceback.domain.payment.dto.PaymentResponseDto;
import com.example.memecommerceback.domain.payment.dto.TossPaymentResponseDto;
import com.example.memecommerceback.domain.payment.entity.Payment;
import com.example.memecommerceback.domain.payment.entity.PaymentStatus;
import com.example.memecommerceback.domain.payment.exception.PaymentCustomException;
import com.example.memecommerceback.domain.payment.exception.PaymentExceptionCode;
import com.example.memecommerceback.domain.payment.repository.PaymentRepository;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import com.example.memecommerceback.global.redis.service.StockLockServiceV1;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImplV1 implements PaymentServiceV1 {

  private final StockLockServiceV1 stockLockService;
  private final TossPaymentServiceV1 tossPaymentService;
  private final OrderProductServiceV1 orderProductService;

  private final PaymentRepository paymentRepository;

  @Override
  @Transactional
  public PaymentResponseDto.ConfirmOneDto confirmOne(
      PaymentRequestDto.ConfirmOneDto requestDto) {
    List<OrderProduct> orderedProductList
        = orderProductService.findAllByOrderId(requestDto.getOrderId());

    // 주문은 무조건 OrderProduct의 어떤 인덱스건, 고유 번호가 지정된 딱 하나의 주문
    Order order = orderedProductList.get(0).getOrder();

    if(!order.getStatus().equals(OrderStatus.WAITING_FOR_PAYMENT)){
      throw new PaymentCustomException(
          PaymentExceptionCode.NOT_WAITING_FOR_PAYMENT_ORDER);
    }

    for (OrderProduct orderedProduct : orderedProductList) {
      Product product = orderedProduct.getProduct();
      stockLockService.tryLockStock(
          product.getId(), orderedProduct.getQuantity());
      Long productStock = product.decreaseStock(orderedProduct.getQuantity());
      if (productStock == 0L) {
        product.updateStatus(ProductStatus.TEMP_OUT_OF_STOCK);
      }
    }

    TossPaymentResponseDto.ConfirmOneDto tossResponse
        = tossPaymentService.confirmOne(requestDto);

    if (tossResponse.isSuccessful()) {
      Payment payment = PaymentConverter.toEntity(
          tossResponse, order, PaymentStatus.SUCCESS);
      paymentRepository.save(payment);
      order.updateStatus(OrderStatus.PAID);

      return PaymentConverter.toConfirmOneDto(
          tossResponse, payment.getStatus());
    }else{
      Payment payment = PaymentConverter.toEntity(
          tossResponse, order, PaymentStatus.FAIL);
      paymentRepository.save(payment);
      order.updateStatus(OrderStatus.FAILED);
      return PaymentConverter.toConfirmOneDto(
          tossResponse, payment.getStatus());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public PaymentResponseDto.ReadOneDto readOne(String paymentKey){
    TossPaymentResponseDto.ReadOneDto tossResponseDto
        = tossPaymentService.readOne(paymentKey);
    // 주문 내역을 삭제하면 안 보이도록 변경
    orderProductService.findByOrderId(
        UUID.fromString(tossResponseDto.getOrderId()));

    Payment payment = findByPaymentKey(paymentKey);

    return PaymentConverter.toReadOneDto(
        tossResponseDto, payment.getStatus());
  }

  @Override
  @Transactional
  public PaymentResponseDto.CancelOneDto cancelOne(
      PaymentRequestDto.CancelOneDto requestDto) {
    return null;
  }

  @Override
  @Transactional
  public Payment findByPaymentKey(String paymentKey) {
    return paymentRepository.findByPaymentKey(paymentKey).orElseThrow(
        () -> new PaymentCustomException(PaymentExceptionCode.NOT_FOUND));
  }
}
