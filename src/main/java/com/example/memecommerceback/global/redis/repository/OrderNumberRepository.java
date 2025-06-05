package com.example.memecommerceback.global.redis.repository;

import com.example.memecommerceback.global.redis.RedisKeyConstants;
import com.example.memecommerceback.global.utils.DateUtils;
import org.springframework.stereotype.Repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Repository
public class OrderNumberRepository {

  @Qualifier("redisTemplateForOrderNumberSequence")
  private final RedisTemplate<String, String> redisTemplate;

  public OrderNumberRepository(
      @Qualifier("redisTemplateForOrderNumberSequence")
      RedisTemplate<String, String> redisTemplate){
    this.redisTemplate = redisTemplate;
  }

  // key -> order:todayIsoDate value -> 00001
  public String getTodayOrderNumber(String key){
    return redisTemplate.opsForValue().get(getRedisKey(key));
  }

  // 주문을 할 때, 주문 번호가 생겨야함.
  // parameter key : todayIsoDate
  // redis key : order:todayIsoDate
  // redis value : String 000001
  public <T> String save(String key) {
    String current = getTodayOrderNumber(key);
    Long lastTodayOrderNumber = (current == null) ? 0L : Long.parseLong(current);
    Long newOrderNumber = lastTodayOrderNumber + 1;
    String newOrderNumberToString = padWithZero(newOrderNumber);

    redisTemplate.opsForValue().set(
        getRedisKey(key), newOrderNumberToString);
    long minutesUntilMidnight = DateUtils.getMinutesUntilMidnight();
    redisTemplate.expire(getRedisKey(key), minutesUntilMidnight, TimeUnit.MINUTES);
    return newOrderNumberToString;
  }


  private String getRedisKey(String key){
    return RedisKeyConstants.ORDER+key;
  }

  private String padWithZero(long number) {
    if (number < 0 || number >= 1000000) {
      throw new NumberFormatException("음수이거나 백만이 넘는 숫자는 불가능합니다.");
    }

    StringBuilder sb = new StringBuilder(8);
    String numberToString = Long.toString(number);

    if (numberToString.length() < 8) {
      int length = 8 - numberToString.length();
      for (int i = 0; i < length; i++) {
        sb.append('0');
      }
    }

    sb.append(numberToString);
    return sb.toString();
  }

}
