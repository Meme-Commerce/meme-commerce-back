package com.example.memecommerceback.domain.user.repository;

import com.example.memecommerceback.domain.user.entity.Users;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, UUID> {
  Optional<Users> findByEmail(String email);
  @Query("SELECT DISTINCT u FROM Users u LEFT JOIN FETCH u.oauthProviderList WHERE u.contact = :contact")
  List<Users> findByContactFetchOAuth(@Param("contact") String contact);
}

