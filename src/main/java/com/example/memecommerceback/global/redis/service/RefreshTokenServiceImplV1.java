package com.example.memecommerceback.global.redis.service;

import com.example.memecommerceback.domain.users.entity.UserRole;
import com.example.memecommerceback.global.jwt.JwtConstants;
import com.example.memecommerceback.global.jwt.JwtUtils;
import com.example.memecommerceback.global.jwt.dto.JwtTokenDto;
import com.example.memecommerceback.global.redis.RedisKeyConstants;
import com.example.memecommerceback.global.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImplV1 implements RefreshTokenServiceV1{

  private final JwtUtils jwtUtils;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  @Transactional
  public <T> void save(String key, Integer minutes, T value) {
    refreshTokenRepository.save(key, minutes, value);
  }

  @Override
  @Transactional(readOnly = true)
  public <T> String getByKey(String key) {
    return refreshTokenRepository.getByKey(key);
  }

  @Override
  @Transactional
  public void deleteToken(String key) {
    refreshTokenRepository.deleteToken(key);
  }
}
