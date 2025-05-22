package com.example.memecommerceback.domain.user.entity;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.OAuth2CustomException;
import lombok.Getter;

@Getter
public enum OAuthProvider {
  KAKAO(oauthProvider.KAKAO),
  NAVER(oauthProvider.NAVER),
  GOOGLE(oauthProvider.GOOGLE),
  ;

  private final Character provider;

  OAuthProvider(Character provider) {
    this.provider = provider;
  }

  public static class oauthProvider {

    public static final Character NAVER = 'n';
    public static final Character KAKAO = 'k';
    public static final Character GOOGLE = 'g';
  }

  public static OAuthProvider fromCode(char provider) {
    for (OAuthProvider o : OAuthProvider.values()) {
      if (o.getProvider().equals(provider)) {
        return o;
      }
    }
    throw new OAuth2CustomException(GlobalExceptionCode.NOT_FOUND_PROVIDER);
  }
}