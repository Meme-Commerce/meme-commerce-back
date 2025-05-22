package com.example.memecommerceback.global.jwt;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.JwtCustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;

@RequiredArgsConstructor
public class CookieUtil {

  private final CookieProperties cookieProperties;

  public ResponseCookie createCookie(
      String name, String value, long maxAgeSeconds){
    return ResponseCookie.from(name, value)
        .domain(cookieProperties.getDomain())
        .sameSite(cookieProperties.getSameSite())
        .secure(cookieProperties.isSecure())
        .httpOnly(cookieProperties.isHttpOnly())
        .maxAge(maxAgeSeconds)
        .path("/")
        .build();
  }

  public void deleteCookie(
      HttpServletResponse response, String cookieName) {
    Cookie cookie = new Cookie(cookieName, null);
    cookie.setHttpOnly(false);
    cookie.setSecure(cookieProperties.isSecure());
    cookie.setPath("/");
    if (cookieProperties.getDomain() != null) {
      cookie.setDomain(cookieProperties.getDomain());
    }
    cookie.setMaxAge(0); // 쿠키 삭제
    response.addCookie(cookie);
  }

  public String getCookieValue(HttpServletRequest request, String cookieName) {
    Cookie cookie = getCookieByName(request, cookieName);
    return cookie != null ? cookie.getValue() : null;
  }

  public String getJwtTokenFromCookie(
      HttpServletRequest request, boolean isAccessToken) {
    String headerName = isAccessToken
        ? JwtConstants.ACCESS_TOKEN_HEADER : JwtConstants.REFRESH_TOKEN_HEADER;

    Cookie cookie = getCookieByName(request, headerName);
    if (cookie != null) {
      return cookie.getValue();
    }
    throw new JwtCustomException(GlobalExceptionCode.MISSING_TOKEN);
  }

  private Cookie getCookieByName(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return null;
    for (Cookie cookie : cookies) {
      if (name.equals(cookie.getName())) {
        return cookie;
      }
    }
    return null;
  }
}
