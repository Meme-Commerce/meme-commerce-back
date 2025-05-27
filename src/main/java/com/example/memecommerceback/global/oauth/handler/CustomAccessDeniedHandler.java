package com.example.memecommerceback.global.oauth.handler;

import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.Error;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    String uri = request.getRequestURI();

    if (uri != null && uri.startsWith("/api")) {
      ErrorResponseDto error = ErrorResponseDto.of(
          Error.AUTH_DENIED_ERROR.getCode(),
          Error.AUTH_DENIED_ERROR.getMessage()
      );
      CommonResponseDto<ErrorResponseDto> body = new CommonResponseDto<>(
          error, accessDeniedException.getMessage(), HttpServletResponse.SC_FORBIDDEN
      );

      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(objectMapper.writeValueAsString(body));
    } else {
      response.sendRedirect("/login");
    }
  }
}
