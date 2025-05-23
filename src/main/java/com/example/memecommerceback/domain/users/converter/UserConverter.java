package com.example.memecommerceback.domain.users.converter;

import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.Gender;
import com.example.memecommerceback.domain.users.entity.OAuthProvider;
import com.example.memecommerceback.domain.users.entity.UserRole;
import com.example.memecommerceback.domain.userOAuthProvider.converter.UserOAuthProviderConverter;
import com.example.memecommerceback.domain.users.entity.User;
import java.time.LocalDate;
import java.util.UUID;

public class UserConverter {

  public static User toEntity(
      String email, String oauthId, OAuthProvider provider, String name,
      String gender, String contact, LocalDate birthDate, Integer age) {
    User user = User.builder()
        .email(email)
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

  public static UserResponseDto.UpdateProfileDto toUpdateProfileDto(User user){
    return UserResponseDto.UpdateProfileDto.builder()
        .userId(user.getId())
        .address(user.getAddress())
        .profileImage(user.getProfileImage())
        .name(user.getName())
        .nickname(user.getNickname())
        .userRole(user.getRole())
        .birthDate(user.getBirthDate().toString())
        .gender(user.getGender())
        .contact(user.getContact())
        .age(user.getAge())
        .build();
  }
}
