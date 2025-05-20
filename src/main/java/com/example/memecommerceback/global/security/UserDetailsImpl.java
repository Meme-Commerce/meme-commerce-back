package com.example.memecommerceback.global.security;

import com.example.memecommerceback.domain.user.entity.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, OAuth2User {

  private final Users user;

  /**
   * 사용자의 권한 정보를 반환합니다.
   *
   * 현재는 권한이 null인 단일 SimpleGrantedAuthority 객체만 포함된 컬렉션을 반환합니다.
   *
   * @return 권한 정보를 담은 컬렉션
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // UserRole role = user.getRole();
    // String authority = role.getAuthority();

    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(null);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(simpleGrantedAuthority);

    return authorities;
  }

  /**
   * 사용자 비밀번호를 반환합니다.
   *
   * 항상 {@code null}을 반환합니다.
   *
   * @return 비밀번호(항상 {@code null})
   */
  @Override
  public String getPassword() {
    return null;
  }

  /**
   * 사용자의 사용자명을 반환합니다.
   *
   * 현재는 항상 null을 반환합니다.
   *
   * @return 사용자명 또는 null
   */
  @Override
  public String getUsername() {
    return null;
    // return this.user.getNickname();
  }

  /**
   * 계정이 만료되지 않았음을 나타냅니다.
   *
   * @return 항상 {@code true}를 반환하여 계정이 만료되지 않았음을 의미합니다.
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 계정이 잠겨 있지 않음을 나타냅니다.
   *
   * @return 항상 {@code true}를 반환하여 계정이 잠겨 있지 않음을 의미합니다.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 사용자의 인증 정보가 만료되지 않았음을 나타냅니다.
   *
   * @return 항상 {@code true}를 반환하여 인증 정보가 만료되지 않았음을 의미합니다.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 사용자의 계정이 활성화되어 있음을 나타냅니다.
   *
   * @return 항상 {@code true}를 반환하여 계정이 활성 상태임을 의미합니다.
   */
  @Override
  public boolean isEnabled() {
    return true;
  }

  /**
   * OAuth2 사용자 속성 정보를 반환합니다.
   *
   * @return 항상 {@code null}을 반환합니다.
   */
  @Override
  public Map<String, Object> getAttributes() {
    return null;
    // return Map.of("email", user.getEmail(), "oauthId", user.getOauthId());
  }

  /**
   * 사용자의 이메일 주소를 반환합니다.
   *
   * 현재는 항상 null을 반환합니다.
   *
   * @return 사용자의 이메일 주소 또는 null
   */
  public String getEmail() {
    return null;
    // return this.user.getEmail();
  }

  /**
   * OAuth2 사용자 고유 식별자를 반환합니다.
   *
   * 현재는 항상 {@code null}을 반환합니다.
   *
   * @return OAuth2 사용자 식별자 또는 {@code null}
   */
  @Override
  public String getName() {
    return null;
    // return user.getOauthId(); // OAuth2 고유 식별자
  }
}
