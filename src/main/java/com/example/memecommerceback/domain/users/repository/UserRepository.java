package com.example.memecommerceback.domain.users.repository;

import com.example.memecommerceback.domain.users.entity.User;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.oauthProviderList WHERE u.contact = :contact")
  Optional<User> findByContactFetchOAuth(@Param("contact") String contact);
  Boolean existsByNickname(String nickname);
}

