package com.example.memecommerceback.global.jwt;

import com.example.memecommerceback.domain.users.entity.UserRole;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.JwtCustomException;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.Error;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.example.memecommerceback.global.jwt.cookie.CookieConstants;
import com.example.memecommerceback.global.jwt.cookie.CookieUtils;
import com.example.memecommerceback.global.jwt.dto.JwtTokenDto;
import com.example.memecommerceback.global.redis.repository.RefreshTokenRepository;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import com.example.memecommerceback.global.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "JWT Utils")
@RequiredArgsConstructor
public class JwtUtils {

  private Key key;

  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  private final CookieUtils cookieUtils;

  private final UserDetailsServiceImpl userDetailsService;

  private final RefreshTokenRepository refreshTokenRepository;

  private static final long ADMIN_LOCAL_ACCESS_EXP = 6 * 60 * 60 * 1000L;
  private static final long ADMIN_LOCAL_REFRESH_EXP = 7 * 24 * 60 * 60 * 1000L;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // 개발 환경에서 어드민 관리자일 때, 토큰 만료 시간 변경
  public long getAccessTokenExpiration(UserRole role) {
    if (isLocal() && UserRole.ADMIN.equals(role)) {
      return ADMIN_LOCAL_ACCESS_EXP;
    }
    return JwtConstants.ACCESS_TOKEN_EXPIRATION;
  }

  public long getRefreshTokenExpiration(UserRole role) {
    if (isLocal() && UserRole.ADMIN.equals(role)) {
      return ADMIN_LOCAL_REFRESH_EXP;
    }
    return JwtConstants.REFRESH_TOKEN_EXPIRATION;
  }

  public boolean isLocal() {
    return "local".equalsIgnoreCase(activeProfile);
  }

  public JwtTokenDto generateToken(String email, UserRole role) {
    Date now = new Date();
    String accessToken = Jwts.builder()
        .setSubject(email)
        .claim(JwtConstants.AUTHORIZATION_KEY, role.getAuthority())
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + getAccessTokenExpiration(role)))
        .signWith(key, signatureAlgorithm)
        .compact();

    String refreshToken = Jwts.builder()
        .setSubject(email)
        .claim(JwtConstants.AUTHORIZATION_KEY, role.getAuthority())
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + getRefreshTokenExpiration(role)))
        .signWith(key, signatureAlgorithm)
        .compact();
    return JwtTokenDto.of(accessToken, refreshToken);
  }

  public JwtStatus validateToken(String tokenValue) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key).build().parseClaimsJws(tokenValue);
      return JwtStatus.ACCESS;
    } catch (UnsupportedJwtException | MalformedJwtException e) {
      log.error(e.getMessage());
      return JwtStatus.FAIL;
    } catch (SignatureException e) {
      log.error(e.getMessage());
      return JwtStatus.FAIL;
    } catch (ExpiredJwtException e) {
      log.error(e.getMessage());
      return JwtStatus.EXPIRED;
    } catch (IllegalArgumentException e) {
      log.error(e.getMessage());
      return JwtStatus.FAIL;
    } catch (Exception e) {
      log.error(e.getMessage());
      return JwtStatus.FAIL;
    }
  }

  public Claims getClaims(String tokenValue) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(tokenValue)
        .getBody();
  }

  public Authentication getAuthentication(String tokenValue) {
    Claims claims;
    try {
      claims = getClaims(tokenValue);
    } catch (ExpiredJwtException e) {
      // 만료된 토큰에서도 Claims를 추출
      claims = e.getClaims();
    }

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(JwtConstants.AUTHORIZATION_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    // 해당 부분 Username으로 load하는 것처럼 보이지만, 실제론 Email이 저장되어있음.
    UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
    return new UsernamePasswordAuthenticationToken(user, null, authorities);
  }

  public void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public void validateRefreshToken(
      String accessTokenValue, String clientRefreshTokenValue, HttpServletResponse response) {
    Authentication authentication = getAuthentication(accessTokenValue);
    UserDetailsImpl userDetails
        = (UserDetailsImpl) authentication.getPrincipal();

    String email = userDetails.getEmail();
    String role = userDetails.getAuthorities()
        .stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElseThrow(() -> new JwtCustomException(GlobalExceptionCode.MISSING_TOKEN));

    String storedRefreshToken = refreshTokenRepository.getByKey(
        JwtConstants.REFRESH_TOKEN_HEADER + ":" + email);

    // 클라이언트 토큰과 저장된 토큰 비교
    if (!storedRefreshToken.equals(clientRefreshTokenValue)) {
      throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
    }

    JwtStatus jwtStatus = validateToken(storedRefreshToken);
    switch (jwtStatus) {
      case ACCESS -> {
        setResponseCookie(email, role, response);
      }
      case EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.EXPIRED_TOKEN);
      case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
    }
  }

  private void setResponseCookie(String email, String role, HttpServletResponse response) {
    String newAccessToken
        = generateToken(email, UserRole.fromAuthority(role)).getAccessToken();

    ResponseCookie newAccessTokenCookie
        = cookieUtils.createCookie(
        JwtConstants.ACCESS_TOKEN_HEADER, newAccessToken,
        getAccessTokenExpiration(UserRole.fromAuthority(role)) / 1000);

    response.addHeader(
        CookieConstants.SET_COOKIE, newAccessTokenCookie.toString());
    setAuthentication(getAuthentication(newAccessToken));
  }

  public void handleJwtErrorResponse(
      HttpServletResponse response, JwtCustomException e) throws IOException {
    log.error("JWT Validation Error: {}", e.getMessage());

    ErrorResponseDto errorResponseDto = ErrorResponseDto.of(e.getErrorCode(), e.getMessage());
    CommonResponseDto<ErrorResponseDto> responseDto = new CommonResponseDto<>(
        errorResponseDto,
        e.getHttpStatus().getReasonPhrase() + " : " + Error.JWT_AUTHENTICATION_ERROR.getMessage(),
        e.getHttpStatus().value()
    );

    response.setStatus(e.getHttpStatus().value());
    response.setContentType("application/json;charset=UTF-8");
    new ObjectMapper().writeValue(response.getWriter(), responseDto);
  }

  public String getEmailFromToken(String token) {
    try {
      Claims claims = getClaims(token);
      return claims.getSubject();
    } catch (ExpiredJwtException e) {
      log.warn("Token has expired: {}", e.getMessage());
      return e.getClaims().getSubject();
    } catch (Exception e) {
      log.error("Error parsing token: {}", e.getMessage());
      throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
    }
  }
}
