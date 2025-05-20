package com.example.memecommerceback.global.security;

import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * 주어진 이메일로 사용자를 조회하여 Spring Security의 UserDetails 객체로 반환합니다.
   *
   * @param email 조회할 사용자의 이메일
   * @return 해당 이메일에 해당하는 사용자의 UserDetails 객체
   * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우 발생
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Users user = userRepository.findByEmail(email).orElseThrow(() ->
        new UsernameNotFoundException("존재하지 않는 사용자입니다.")
    );

    return new UserDetailsImpl(user);
  }
}

