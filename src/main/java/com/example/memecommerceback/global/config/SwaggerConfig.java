package com.example.memecommerceback.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j(topic = "swagger config")
@Configuration
public class SwaggerConfig {

  @Value("${my-config.email}")
  private String myEmail;

  /**
   * Swagger(OpenAPI) 문서 생성을 위한 OpenAPI 인스턴스를 설정합니다.
   *
   * API 메타데이터, JWT 기반 인증 스키마, 서버 URL, 보안 요구사항 등을 포함하여 Swagger UI에서 API 문서를 제공합니다.
   *
   * @return Swagger UI 및 OpenAPI 문서 생성을 위한 OpenAPI 객체
   */
  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("BackOffice API Document")
        .version("1.0")
        .description("my backoffice project description")
        .contact(
            new io.swagger.v3.oas.models.info.Contact().email(
                myEmail));
    String jwtScheme = "jwtAuth";
    SecurityRequirement securityRequirement
        = new SecurityRequirement().addList(jwtScheme);
    Components components = new Components()
        .addSecuritySchemes(jwtScheme, new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .scheme("Bearer")
            .bearerFormat("JWT"));

    // Swagger UI 설정 및 보안 추가
    return new OpenAPI()
        .addServersItem(new Server().url("http://localhost:8080"))  // 추가적인 서버 URL 설정 가능
        .components(components)
        .info(info)
        .addSecurityItem(securityRequirement);
  }
}
