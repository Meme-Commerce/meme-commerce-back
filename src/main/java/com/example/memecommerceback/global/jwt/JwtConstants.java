package com.example.memecommerceback.global.jwt;

public class JwtConstants {
  public static final String BEARER_PREFIX = "Bearer ";
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String ACCESS_TOKEN_HEADER = "accessToken";
  public static final String REFRESH_TOKEN_HEADER = "refreshToken";
  public static final String AUTHORIZATION_KEY = "auth";
  public static final Long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L;
  public static final Long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000L;

  // 인스턴스화 방지
  private JwtConstants(){

  }
}
