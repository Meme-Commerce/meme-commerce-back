package com.example.memecommerceback.global.oauth.handler;

import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.info("로그아웃 성공!");
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");

    CommonResponseDto<Void> successResponse = new CommonResponseDto<>(
        null, "로그아웃 성공", HttpServletResponse.SC_OK);
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(successResponse));

    response.getWriter().flush();
  }
}
