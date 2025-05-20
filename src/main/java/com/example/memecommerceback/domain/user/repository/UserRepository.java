package com.example.memecommerceback.domain.user.repository;


import com.example.memecommerceback.domain.user.entity.Users;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, UUID> {

  Optional<Users> findByEmail(String email);
}

