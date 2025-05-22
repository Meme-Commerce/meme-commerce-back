package com.example.memecommerceback.domain.userOAuthProvider.repository;

import com.example.memecommerceback.domain.userOAuthProvider.entity.UserOAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOAuthProviderRepository extends JpaRepository<UserOAuthProvider, Long> {

}
