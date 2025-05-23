package com.example.memecommerceback.global.redis.service;

public interface RefreshTokenServiceV1 {
  <T> void save(String key, Integer minutes, T value);
  <T> String getByKey(String key);

  void deleteToken(String key);
}
