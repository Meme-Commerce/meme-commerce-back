package com.example.memecommerceback.global.oauth.handler;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.JwtCustomException;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.Error;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.example.memecommerceback.global.jwt.JwtConstants;
import com.example.memecommerceback.global.jwt.JwtStatus;
import com.example.memecommerceback.global.jwt.JwtUtils;
import com.example.memecommerceback.global.jwt.cookie.CookieUtils;
import com.example.memecommerceback.global.redis.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "Logout process")
public class CustomLogoutHandler implements LogoutHandler {

  private final JwtUtils jwtUtils;
  private final CookieUtils cookieUtils;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    log.info("logout 진행중...");

    String accessToken
        = cookieUtils.getCookieValue(
        request, JwtConstants.ACCESS_TOKEN_HEADER);
    String refreshToken
        = cookieUtils.getCookieValue(
        request, JwtConstants.REFRESH_TOKEN_HEADER);

    try {
      JwtStatus accessStatus = jwtUtils.validateToken(accessToken);
      JwtStatus refreshStatus = jwtUtils.validateToken(refreshToken);

      // 위조된 경우에만 예외 처리 -> 해당 부분은 AuditLogs를 통하여 상세한 오류 로그 구현 예정
      if (accessStatus == JwtStatus.FAIL || refreshStatus == JwtStatus.FAIL) {
        throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
      }

      if (accessStatus == JwtStatus.ACCESS) {
        String email = jwtUtils.getEmailFromToken(accessToken);
        String redisKey = JwtConstants.REFRESH_TOKEN_HEADER + ":" + email;

        if (refreshTokenRepository.getByKey(redisKey) != null) {
          refreshTokenRepository.deleteToken(redisKey);
          log.info("delete refresh token success!");
        }
      }

      if (refreshStatus == JwtStatus.ACCESS
          || refreshStatus == JwtStatus.EXPIRED) {
        String email = jwtUtils.getEmailFromToken(refreshToken);
        String redisKey = JwtConstants.REFRESH_TOKEN_HEADER + ":" + email;
            if (refreshTokenRepository.getByKey(redisKey) != null) {
              refreshTokenRepository.deleteToken(redisKey);
              log.info("delete refresh token by refresh token success!");
            }
      }
    } catch (Exception e) {
      log.warn("logout 중 예외 발생: {}", e.getMessage());

      int status = HttpServletResponse.SC_UNAUTHORIZED;
      String message = Error.LOGOUT_ERROR.getMessage();
      String errorCode = Error.LOGOUT_ERROR.getCode();

      ErrorResponseDto errorResponse = null;
      CommonResponseDto<ErrorResponseDto> commonResponseDto = null;

      response.setStatus(status);
      response.setContentType("application/json;charset=UTF-8");

      if(e instanceof JwtCustomException jwtEx){
        message = Error.JWT_AUTHENTICATION_ERROR.getMessage();
        errorCode = Error.JWT_AUTHENTICATION_ERROR.getCode();
        errorResponse = ErrorResponseDto.of(errorCode, jwtEx.getMessage());
        commonResponseDto = new CommonResponseDto<>(errorResponse, message, status);
      }else{
        errorResponse = ErrorResponseDto.of(errorCode, e.getMessage());
        commonResponseDto = new CommonResponseDto<>(errorResponse, message, status);
      }

      try {
        response.getWriter().write(
            new ObjectMapper().writeValueAsString(commonResponseDto)
        );
        response.getWriter().flush();
      } catch (IOException ioException) {
        log.error("로그아웃 에러 응답 전송 중 오류: {}", ioException.getMessage());
      }
      return;
    } finally {
      cookieUtils.deleteCookie(response, JwtConstants.ACCESS_TOKEN_HEADER);
      cookieUtils.deleteCookie(response, JwtConstants.REFRESH_TOKEN_HEADER);
      log.info("delete cookie in server success!");
    }
  }
}
