package com.example.memecommerceback.domain.users.converter;

import com.example.memecommerceback.domain.files.converter.FileConverter;
import com.example.memecommerceback.domain.files.entity.File;
import com.example.memecommerceback.domain.userOAuthProvider.converter.UserOAuthProviderConverter;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.Gender;
import com.example.memecommerceback.domain.users.entity.OAuthProvider;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.entity.UserRole;
import java.time.LocalDate;
import java.util.List;

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

  public static UserResponseDto.UpdateProfileDto toUpdateProfileDto(User user) {
    return UserResponseDto.UpdateProfileDto.builder()
        .userId(user.getId())
        .address(user.getAddress())
        .profileImage(user.getProfileImage())
        .name(user.getName())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .userRole(user.getRole())
        .birthDate(user.getBirthDate().toString())
        .gender(user.getGender())
        .contact(user.getContact())
        .age(user.getAge())
        .build();
  }

  public static UserResponseDto.IsAvailableNicknameDto toIsAvailableNicknameDto(
      String requestNickname, boolean isAvailable) {
    return UserResponseDto.IsAvailableNicknameDto.builder()
        .requestNickname(requestNickname)
        .isAvailable(isAvailable)
        .build();
  }

  public static UserResponseDto.ReadProfileDto toReadProfileDto(
      User user) {
    return UserResponseDto.ReadProfileDto.builder()
        .userId(user.getId())
        .address(user.getAddress())
        .profileImage(user.getProfileImage())
        .name(user.getName())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .userRole(user.getRole())
        .birthDate(user.getBirthDate().toString())
        .gender(user.getGender())
        .contact(user.getContact())
        .age(user.getAge())
        .createdAt(user.getCreatedAt())
        .profileImage(user.getProfileImage())
        .address(user.getAddress())
        .registeredCompanyName(user.getCompanyName())
        .build();
  }

  public static UserResponseDto.UpdateRoleDto toUpdateRoleDto(
      User user, List<File> fileList) {
    return UserResponseDto.UpdateRoleDto.builder()
        .userId(user.getId())
        .userRole(user.getRole())
        .name(user.getName())
        .nickname(user.getNickname())
        .fileResponseDtoList(FileConverter.toResponseDtoList(fileList))
        .build();
  }
}
