package com.example.memecommerceback.global.redis.repository;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.JsonCustomException;
import com.example.memecommerceback.global.exception.JwtCustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenRepository {

  private final ObjectMapper objectMapper;

  // database 0 : jwt token -> refreshToken
  @Qualifier("redisTemplateForToken")
  private final RedisTemplate<String, Object> redisTemplateForToken;

  public RefreshTokenRepository(
      ObjectMapper objectMapper,
      @Qualifier("redisTemplateForToken") RedisTemplate<String, Object> redisTemplateForToken) {
    this.objectMapper = objectMapper;
    this.redisTemplateForToken = redisTemplateForToken;
  }

  public <T> void save(String key, Integer minutes, T value) {
    String valueString = null;
    try {
      valueString =
          !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
    } catch (JsonProcessingException e) {
      throw new JsonCustomException(GlobalExceptionCode.FAILED_SERIALIZATION);
    }

    redisTemplateForToken.opsForValue().set(key, valueString);
    redisTemplateForToken.expire(key, minutes, TimeUnit.MINUTES);
  }

  public <T> String getByKey(String key) {
    Object value = redisTemplateForToken.opsForValue().get(key);
    if (value== null) {
      throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_REFRESH_TOKEN);
    }
    return value.toString();
  }

}