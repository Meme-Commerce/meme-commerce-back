package com.example.memecommerceback.domain.user.converter;

import com.example.memecommerceback.domain.user.entity.Gender;
import com.example.memecommerceback.domain.user.entity.OAuthProvider;
import com.example.memecommerceback.domain.user.entity.UserRole;
import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.domain.userOAuthProvider.converter.UserOAuthProviderConverter;
import java.time.LocalDate;
import java.util.UUID;

public class UsersConverter {

  public static Users toEntity(
      String email, String oauthId, OAuthProvider provider, String name,
      String gender, String contact, LocalDate birthDate, Integer age) {
    Users user = Users.builder()
        .id(UUID.randomUUID())
        .email(email)
        .oauthId(oauthId)
        .role(UserRole.USER)
        .name(name)
        .gender(Gender.fromCode(gender))
        .contact(contact)
        .birthDate(birthDate)
        .age(age)
        .build();

    user.addOAuthProvider(
        UserOAuthProviderConverter.toEntity(user, oauthId, provider));
    return user;
  }
}
