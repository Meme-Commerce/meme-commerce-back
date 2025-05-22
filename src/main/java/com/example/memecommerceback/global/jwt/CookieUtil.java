package com.example.memecommerceback.global.jwt;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.JwtCustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

  @Value("${cookie.secure}")
  private boolean isSecure;

  public ResponseCookie createCookie(
      String name, String value, long maxAgeSeconds){
    if(!isSecure){
      // local test success
      return ResponseCookie.from(name, value)
          .httpOnly(false)        // 로컬 환경에서 httpOnly 또한 false로 변경
          .secure(this.isSecure) // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
          .path("/") // 쿠키가 적용될 경로
          .maxAge(maxAgeSeconds) // 쿠키의 유효 기간 설정 (초 단위)
          .sameSite("Lax") // CSRF 보호를 위한 SameSite 설정
          .build();
    }else {
      // production
      return ResponseCookie.from(name, value)
          .httpOnly(true)
          .secure(this.isSecure) // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
          .path("/") // 쿠키가 적용될 경로
          .maxAge(maxAgeSeconds) // 쿠키의 유효 기간 설정 (초 단위)
          .domain(".your_domain.com")
          .sameSite("Strict")// CSRF 보호를 위한 SameSite 설정
          .build();
    }
  }

  public void deleteCookie(HttpServletResponse response, String cookieName) {
    if(!isSecure){
      // 로컬
      Cookie cookie = new Cookie(cookieName, null);
      cookie.setHttpOnly(false);
      cookie.setSecure(this.isSecure); // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
      cookie.setPath("/");
      cookie.setMaxAge(0); // 쿠키 삭제
      response.addCookie(cookie);
    }else{
      // 배포
      Cookie cookie = new Cookie(cookieName, null);
      cookie.setHttpOnly(true);
      cookie.setSecure(this.isSecure); // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
      cookie.setPath("/");
      cookie.setMaxAge(0); // 쿠키 삭제
      response.addCookie(cookie);
    }
  }

  public String getCookieValue(HttpServletRequest request, String cookieName){
    Cookie[] cookieList = request.getCookies();
    if(cookieList != null){
      for (Cookie cookie : cookieList){
        if (cookieName.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  public String getJwtTokenFromCookie(HttpServletRequest request, boolean isAccessToken){
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if(isAccessToken){
          if (JwtConstants.ACCESS_TOKEN_HEADER.equals(cookie.getName())) {
            return cookie.getValue();
          }
        }else{
          if (JwtConstants.REFRESH_TOKEN_HEADER.equals(cookie.getName())) {
            return cookie.getValue();
          }
        }
      }
    }
    throw new JwtCustomException(GlobalExceptionCode.MISSING_TOKEN);
  }
}
