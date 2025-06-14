package com.example.memecommerceback.global.redis.repository;

import com.example.memecommerceback.global.redis.RedisKeyConstants;
import com.example.memecommerceback.global.redis.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class StockLockRepository {

  @Qualifier("redisTemplateForStockLock")
  private final RedisTemplate<String, String> redisTemplate;

  public StockLockRepository(
      @Qualifier("redisTemplateForStockLock") RedisTemplate<String, String> redisTemplate){
    this.redisTemplate = redisTemplate;
  }

  public void save(UUID productId, Long quantity) {
    String key = RedisKeyUtils.getProductStockKey(productId);
    redisTemplate.opsForValue().set(key, String.valueOf(quantity));
  }

  public Long executeStockLock(UUID productId, Long quantity) {
    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
    redisScript.setScriptText(RedisKeyConstants.LUA_SCRIPT);
    redisScript.setResultType(Long.class);

    String key = RedisKeyUtils.getProductStockKey(productId);
    return redisTemplate.execute(redisScript, List.of(key), String.valueOf(quantity));
  }

  public Long restoreStock(UUID productId, Long quantity){
    String key = RedisKeyUtils.getProductStockKey(productId);
    try {
      Long result = redisTemplate.opsForValue().increment(key, quantity);
      if (result == null) {
        throw new IllegalStateException("재고 복원에 실패했습니다: " + productId);
      }
    } catch (Exception e) {
      throw new RuntimeException("재고 복원 중 오류가 발생했습니다: " + productId, e);
    }
    return redisTemplate.opsForValue().increment(key, quantity);
  }
}
