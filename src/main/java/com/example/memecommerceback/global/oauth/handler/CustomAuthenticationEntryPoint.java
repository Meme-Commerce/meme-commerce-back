package com.example.memecommerceback.global.oauth.handler;

import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.Error;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    String uri = request.getRequestURI();

    if (uri != null && uri.equals("/login")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write("{\"error\":\"Unauthorized\"}");
      return;
    }

    if (uri != null && uri.startsWith("/api/")) {
      ErrorResponseDto error = ErrorResponseDto.of(
          Error.JWT_AUTHENTICATION_ERROR.getCode(),
          Error.JWT_AUTHENTICATION_ERROR.getMessage()
      );
      CommonResponseDto<ErrorResponseDto> body = new CommonResponseDto<>(
          error, authException.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(objectMapper.writeValueAsString(body));
    }
  }
}
