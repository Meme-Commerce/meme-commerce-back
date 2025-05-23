package com.example.memecommerceback.domain.userOAuthProvider.converter;

import com.example.memecommerceback.domain.userOAuthProvider.entity.UserOAuthProvider;
import com.example.memecommerceback.domain.users.entity.OAuthProvider;
import com.example.memecommerceback.domain.users.entity.User;

public class UserOAuthProviderConverter {

  public static UserOAuthProvider toEntity(
      User user, String providerId, OAuthProvider provider) {
    return UserOAuthProvider.builder()
        .user(user)
        .providerId(providerId)
        .provider(provider)
        .build();
  }
}
