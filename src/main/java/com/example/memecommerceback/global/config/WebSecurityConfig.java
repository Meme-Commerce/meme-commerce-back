package com.example.memecommerceback.global.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebSecurityConfig {
  // 쓸 곳 없는 메서드와 메서드 명을 한글로 적기
  void 테스트(){
    // System.out.println -> 불필요한 구문 잡아내는지?
    System.out.println("안녕? 코드 레빗");
  }

  // 해당 컴피그 파일에 필요 없는 메서드
  public Authentication getAuthentication(){
    // 변수 선언을 할 필요가 없었지만, 하는 경우
    Authentication authentication
        = SecurityContextHolder.getContext().getAuthentication();
    return authentication;
  }
}
