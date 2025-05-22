package com.example.memecommerceback.global.oauth.handler;

import com.example.memecommerceback.domain.user.entity.UserRole;
import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.global.jwt.JwtConstants;
import com.example.memecommerceback.global.jwt.JwtUtils;
import com.example.memecommerceback.global.jwt.cookie.CookieConstants;
import com.example.memecommerceback.global.jwt.cookie.CookieUtils;
import com.example.memecommerceback.global.oauth.constant.OAuthConstants;
import com.example.memecommerceback.global.oauth.entity.CustomOAuth2User;
import com.example.memecommerceback.global.redis.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtils jwtUtils;
  private final CookieUtils cookieUtils;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    log.info("OAuth2 로그인 성공");

    // OAuth2User → 커스텀 User 엔티티로 캐스팅
    CustomOAuth2User oAuth2User
        = (CustomOAuth2User) authentication.getPrincipal();
    Users user = oAuth2User.getUser();
    String email = user.getEmail();
    UserRole role = user.getRole();

    // Access, Refresh 토큰 발급
    String accessToken
        = jwtUtils.generateToken(email, role).getAccessToken();
    String refreshToken
        = jwtUtils.generateToken(email, role).getRefreshToken();

    refreshTokenRepository.save(
        JwtConstants.REFRESH_TOKEN_HEADER + ":" + email,
        Math.toIntExact(jwtUtils.getRefreshTokenExpiration(role)),
        refreshToken);

    Date now = new Date();
    // 토큰 쿠키로 전송
    ResponseCookie accessTokenCookie
        = cookieUtils.createCookie(
        JwtConstants.ACCESS_TOKEN_HEADER,
        accessToken, now.getTime() + jwtUtils.getAccessTokenExpiration(role));
    ResponseCookie refreshTokenCookie
        = cookieUtils.createCookie(
        JwtConstants.REFRESH_TOKEN_HEADER,
        refreshToken, now.getTime() + jwtUtils.getRefreshTokenExpiration(role));

    response.addHeader(
        CookieConstants.SET_COOKIE, accessTokenCookie.toString());
    response.addHeader(
        CookieConstants.SET_COOKIE, refreshTokenCookie.toString());

    response.sendRedirect(OAuthConstants.DEVELOPMENT_SETTINGS_REDIRECT_URL);

    log.info("액세스 토큰과 리프레시 토큰 쿠키로 전송 완료");
  }
}
