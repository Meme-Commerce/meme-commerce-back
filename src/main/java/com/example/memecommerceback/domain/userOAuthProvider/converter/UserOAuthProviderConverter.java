package com.example.memecommerceback.domain.userOAuthProvider.converter;

import com.example.memecommerceback.domain.user.entity.OAuthProvider;
import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.domain.userOAuthProvider.entity.UserOAuthProvider;

public class UserOAuthProviderConverter {

  public static UserOAuthProvider toEntity(
      Users user, String providerId, OAuthProvider provider) {
    return UserOAuthProvider.builder()
        .user(user)
        .providerId(providerId)
        .provider(provider)
        .build();
  }
}
