package com.example.memecommerceback.global.redis.service;

import com.example.memecommerceback.domain.orders.exception.OrderCustomException;
import com.example.memecommerceback.domain.orders.exception.OrderExceptionCode;
import com.example.memecommerceback.global.redis.repository.StockLockRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockLockServiceImplV1 implements StockLockServiceV1{

  private final StockLockRepository stockLockRepository;

  @Override
  @Transactional(readOnly = true)
  public void tryLockStock(UUID productId, Long quantity) {
    Long result = stockLockRepository.executeStockLock(productId, quantity);
    if (result == null || result != 1L) {
      throw new OrderCustomException(OrderExceptionCode.STOCK_LOCK_FAILED);
    }
  }

  @Override
  @Transactional
  public void save(UUID productId, Long quantity) {
    stockLockRepository.save(productId, quantity);
  }

  @Override
  @Transactional
  public Long restoreStock(UUID productId, Long quantity){
    return stockLockRepository.restoreStock(productId, quantity);
  }
}
