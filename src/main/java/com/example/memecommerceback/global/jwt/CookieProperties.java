package com.example.memecommerceback.global.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "cookie")
public class CookieProperties {
  private String domain;
  private String sameSite;
  private boolean httpOnly;
  private boolean secure;

  public CookieProperties(String domain, String sameSite, boolean httpOnly, boolean secure) {
    this.domain = domain;
    this.sameSite = sameSite;
    this.httpOnly = httpOnly;
    this.secure = secure;
  }

  // application.yml은 런타임 때 바뀌지 않는다.
  // application.yml에 아무리 setDomain을 해도 똑같다.
}
