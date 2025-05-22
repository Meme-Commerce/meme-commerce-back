package com.example.memecommerceback.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtTokenDto {
  private final String accessToken;
  private final String refreshToken;

  public static JwtTokenDto of(String accessToken) {
    return new JwtTokenDto(accessToken, null);
  }

  public static JwtTokenDto of(String accessToken, String refreshToken) {
    return new JwtTokenDto(accessToken, refreshToken);
  }
}
