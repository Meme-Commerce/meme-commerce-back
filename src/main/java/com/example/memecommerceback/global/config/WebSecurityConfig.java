package com.example.memecommerceback.global.config;

import com.example.memecommerceback.global.jwt.JwtAuthorizationFilter;
import com.example.memecommerceback.global.jwt.JwtUtils;
import com.example.memecommerceback.global.jwt.cookie.CookieUtils;
import com.example.memecommerceback.global.oauth.handler.CustomAccessDeniedHandler;
import com.example.memecommerceback.global.oauth.handler.CustomAuthenticationEntryPoint;
import com.example.memecommerceback.global.oauth.handler.CustomLogoutHandler;
import com.example.memecommerceback.global.oauth.handler.CustomLogoutSuccessHandler;
import com.example.memecommerceback.global.oauth.handler.OAuth2SuccessHandler;
import com.example.memecommerceback.global.oauth.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtUtils jwtUtils;
  private final CookieUtils cookieUtils;
  private final ObjectMapper objectMapper;

  private final CustomLogoutHandler customLogoutHandler;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter(jwtUtils, cookieUtils);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(cors -> cors
            .configurationSource(request -> {
              CorsConfiguration configuration = new CorsConfiguration();
              configuration.setAllowedOriginPatterns(
                  Arrays.asList(
                      "http://localhost:5200", "http://localhost:8080"));
              configuration.setAllowedMethods(Arrays.asList(
                  "GET", "POST", "PATCH", "DELETE", "OPTIONS"));
              configuration.setAllowedHeaders(Arrays.asList(
                  "Authorization", "refreshToken",
                  "Cache-Control", "Content-Type"));
              configuration.setAllowCredentials(true);
              configuration.setExposedHeaders(Arrays.asList(
                  "Authorization","Set-Cookie"));
              return configuration;
            })
        )
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/api/v1/public/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/hashtags").permitAll()
            .requestMatchers("/login").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement((session) ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService) // 사용자 정보 처리
            )
            .successHandler(oAuth2SuccessHandler) // 로그인 성공 후 처리
        )
        .logout((logout) -> logout
            .logoutUrl("/api/v1/logout")
            .addLogoutHandler(customLogoutHandler)
            .deleteCookies("remember-me")
            .logoutSuccessHandler(customLogoutSuccessHandler)
        )
        .exceptionHandling(e -> e
            .defaultAuthenticationEntryPointFor(
                new CustomAuthenticationEntryPoint(objectMapper),
                new AntPathRequestMatcher("/api/**")
            )
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new AntPathRequestMatcher("/**") // 나머지 모든 요청
            )
            .defaultAccessDeniedHandlerFor(
                new CustomAccessDeniedHandler(objectMapper),
                new AntPathRequestMatcher("/api/**")
            )
            .defaultAccessDeniedHandlerFor(
                new AccessDeniedHandlerImpl(),
                new AntPathRequestMatcher("/**")))
        .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
