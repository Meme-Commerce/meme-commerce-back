package com.example.memecommerceback.global.redis.service;

import com.example.memecommerceback.domain.orders.exception.OrderCustomException;
import java.util.UUID;

public interface StockLockServiceV1 {
  void lockStock(UUID productId, Long quantity) throws OrderCustomException;

  void save(UUID productId, Long quantity);

  /**
   * 결제 완료한 상품의 결제 취소 시, 재고 수량 복원
   * @param productId : 재고를 복원할 주문 아이디
   * @param quantity : 복원한 수량
   * @return 상품 총 재고 수량
   */
  Long restoreStock(UUID productId, Long quantity);
}
