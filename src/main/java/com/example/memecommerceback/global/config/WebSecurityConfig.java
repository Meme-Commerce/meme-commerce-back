package com.example.memecommerceback.global.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebSecurityConfig {

  /**
   * "안녕? 코드 레빗"이라는 메시지를 표준 출력에 출력합니다.
   */
  void 테스트() {
    // System.out.println -> 불필요한 구문 잡아내는지?
    System.out.println("안녕? 코드 레빗");
  }

  /**
   * 현재 보안 컨텍스트에서 인증 정보를 반환합니다.
   *
   * @return 현재 사용자의 Authentication 객체
   */
  public Authentication getAuthentication() {
    // 변수 선언을 할 필요가 없었지만, 하는 경우
    Authentication authentication
        = SecurityContextHolder.getContext().getAuthentication();
    return authentication;
  }

  /**
   * "안녕? 코드 레빗"이라는 메시지를 표준 출력에 출력합니다.
   */
  void 테스트2() {
    // System.out.println -> 불필요한 구문 잡아내는지?
    System.out.println("안녕? 코드 레빗");
  }

  /**
   * 현재 보안 컨텍스트에서 인증 정보를 반환합니다.
   *
   * @return 현재 사용자의 Authentication 객체
   */
  public Authentication getAuthentication2() {
    // 변수 선언을 할 필요가 없었지만, 하는 경우
    Authentication authentication
        = SecurityContextHolder.getContext().getAuthentication();
    return authentication;
  }

  /**
   * "안녕? 코드 레빗"이라는 메시지를 표준 출력에 출력합니다.
   */
  void 테스트3() {
    // System.out.println -> 불필요한 구문 잡아내는지?
    System.out.println("안녕? 코드 레빗");
  }

  /**
   * 현재 보안 컨텍스트에서 인증(Authentication) 객체를 반환합니다.
   *
   * @return 현재 SecurityContext에 저장된 Authentication 객체
   */
  public Authentication getAuthentication3() {
    // 변수 선언을 할 필요가 없었지만, 하는 경우
    Authentication authentication
        = SecurityContextHolder.getContext().getAuthentication();
    return authentication;
  }
}
