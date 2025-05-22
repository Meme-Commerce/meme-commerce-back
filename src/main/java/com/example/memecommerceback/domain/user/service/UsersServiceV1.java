package com.example.memecommerceback.domain.user.service;

import com.example.memecommerceback.domain.user.entity.Users;
import java.util.List;

public interface UsersServiceV1 {

  void save(Users user);
  List<Users> findByContactFetchOAuth(String contact);
}
