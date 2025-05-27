package com.example.memecommerceback.global.jwt;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.JwtCustomException;
import com.example.memecommerceback.global.jwt.cookie.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j(topic = "JWT 검증 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final CookieUtils cookieUtils;

  private final Set<String> loggedRequests = ConcurrentHashMap.newKeySet();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    if (loggedRequests.add(path)) {
      log.info("Filtering path: {}", path);
    }
    return path.startsWith("/oauth2/") || path.startsWith("/login/oauth2") || path.startsWith(
        "/login");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (shouldNotFilter(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String accessTokenValue = cookieUtils.getJwtTokenFromCookie(
          request, true);
      switch (jwtUtils.validateToken(accessTokenValue)) {
        case ACCESS -> {
          jwtUtils.setAuthentication(
              jwtUtils.getAuthentication(accessTokenValue));
          filterChain.doFilter(request, response);
        }
        case EXPIRED -> {
          String refreshTokenValue
              = cookieUtils.getJwtTokenFromCookie(request, false);
          jwtUtils.validateRefreshToken(accessTokenValue, refreshTokenValue, response);
        }
        case FAIL -> throw new JwtCustomException(GlobalExceptionCode.MISSING_TOKEN);
      }
    } catch (JwtCustomException e) {
      jwtUtils.handleJwtErrorResponse(response, e);
    }
  }
}
