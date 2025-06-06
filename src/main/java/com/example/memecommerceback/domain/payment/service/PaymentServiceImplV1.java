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
    // 1. 주문한 상품 리스트 조회 (Product, Order, OrderProduct 모두 Eager)
    List<OrderProduct> orderedProductList
        = orderProductService.findAllByOrderId(requestDto.getOrderId());

    // 2. 주문은 무조건 OrderProduct의 어떤 인덱스건, 고유 번호가 지정된 딱 하나의 주문
    Order order = orderedProductList.get(0).getOrder();

    // 3. 주문 상태가 결제 대기 중인 상품이 아니면 예외
    if(!order.getStatus().equals(OrderStatus.WAITING_FOR_PAYMENT)){
      throw new PaymentCustomException(
          PaymentExceptionCode.NOT_WAITING_FOR_PAYMENT_ORDER);
    }

    // 4. 주문한 상품의 결제 프로세스 진행 및 재고락 추가
    for (OrderProduct orderedProduct : orderedProductList) {
      Product product = orderedProduct.getProduct();
      stockLockService.lockStock(
          product.getId(), orderedProduct.getQuantity());
      product.decreaseStock(orderedProduct.getQuantity());
    }

    // 5. 토스 페이먼츠에 결제 요청
    TossPaymentResponseDto.ConfirmOneDto tossResponse
        = tossPaymentService.confirmOne(requestDto);

    // 6. 성공 또는 실패 시 각각의 상태를 DB에 저장
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
    orderProductService.findFirstByOrderId(
        UUID.fromString(tossResponseDto.getOrderId()));

    Payment payment = findByPaymentKey(paymentKey);

    return PaymentConverter.toReadOneDto(
        tossResponseDto, payment.getStatus());
  }

  // 반품 또는 취소 요청
  @Override
  @Transactional
  public PaymentResponseDto.CancelOneDto cancelOne(
      PaymentRequestDto.CancelOneDto requestDto) {
    // 1. 페이먼츠 키로 주문한 상품 내역 조회
    Payment payment = findByPaymentKey(requestDto.getPaymentKey());

    // 2. 이미 완료된(취소) 결제 내역이면 예외
    if (payment.getStatus() == PaymentStatus.CANCELED) {
      throw new PaymentCustomException(
          PaymentExceptionCode.ALREADY_CANCELED_PAYMENT);
    }

    // 3. 토스 페이먼츠 먼저 취소 요청
    TossPaymentResponseDto.CancelOneDto tossResponse =
        tossPaymentService.cancelOne(requestDto);

    // 4. 주문된 상품 환불 처리 요청
    List<OrderProduct> orderedProductList
        = orderProductService.findAllByOrderId(payment.getOrder().getId());
    List<Product> productList
        = orderedProductList.stream()
        .map(OrderProduct::getProduct)
        .toList();
    orderProductService.validateOrderProductCountMatch(
        orderedProductList.size(), productList.size());

    payment.updateStatus(PaymentStatus.CANCELED);


    // 5. 재고락 레포지토리에서 취소한 상품 갯수만큼 복원
    for(int i = 0; i <orderedProductList.size(); i++){
      OrderProduct orderProduct = orderedProductList.get(i);
      Product product = orderedProductList.get(i).getProduct();
      Long totalProductStockQuantity
          = stockLockService.restoreStock(product.getId(), orderProduct.getQuantity());
      // 6. 재고가 null || 0 미만이면 오류
      if (totalProductStockQuantity == null || totalProductStockQuantity < 0) {
        // 관리자 Redis 복구 실패 비상 알림 추가
        log.error("[Redis 복구 실패] 상품 ID: {}", product.getId());
      }

      // 7. 상품에 등록된 재고와 레디스에 등록된 재고량이 맞지 않으면 오류
      product.increaseStock(orderProduct.getQuantity());
      if (!totalProductStockQuantity.equals(product.getStock())) {
        log.warn("[재고 불일치] Redis={}, DB={}",
            totalProductStockQuantity, product.getStock());
        // 관리자 비상 알림 로직 추가
      }

    }
    // 취소 요청 반환 DTO
    return PaymentConverter.toCancelOneDto(tossResponse);
  }

  @Override
  @Transactional
  public Payment findByPaymentKey(String paymentKey) {
    return paymentRepository.findByPaymentKey(paymentKey).orElseThrow(
        () -> new PaymentCustomException(PaymentExceptionCode.NOT_FOUND));
  }
}
