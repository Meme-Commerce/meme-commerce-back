package com.example.memecommerceback.domain.user.service;

import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.domain.user.repository.UsersRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersServiceImplV1 implements UsersServiceV1 {

  private final UsersRepository usersRepository;

  @Override
  @Transactional
  public void save(Users user) {
    usersRepository.save(user);
  }

  @Override
  @Transactional
  public List<Users> findByContactFetchOAuth(String contact) {
    return usersRepository.findByContactFetchOAuth(contact);
  }
}
