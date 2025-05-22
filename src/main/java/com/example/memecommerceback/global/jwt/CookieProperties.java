package com.example.memecommerceback.global.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cookie")
public class CookieProperties {
  private final String domain;
  private final String sameSite;
  private final boolean httpOnly;
  private final boolean secure;

  public CookieProperties(String domain, String sameSite, boolean httpOnly, boolean secure) {
    this.domain = domain;
    this.sameSite = sameSite;
    this.httpOnly = httpOnly;
    this.secure = secure;
  }
}
