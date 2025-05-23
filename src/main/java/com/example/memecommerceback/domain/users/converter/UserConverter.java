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
        .email(user.getEmail())
        .nickname(user.getNickname())
        .userRole(user.getRole())
        .birthDate(user.getBirthDate().toString())
        .gender(user.getGender())
        .contact(user.getContact())
        .age(user.getAge())
        .build();
  }

  /**
   * 요청한 닉네임의 사용 가능 여부를 나타내는 DTO를 생성합니다.
   *
   * @param requestNickname 확인할 닉네임
   * @param isAvailable 닉네임 사용 가능 여부
   * @return 닉네임과 사용 가능 여부 정보를 담은 IsAvailableNicknameDto 객체
   */
  public static UserResponseDto.IsAvailableNicknameDto toIsAvailableNicknameDto(
      String requestNickname, boolean isAvailable){
    return UserResponseDto.IsAvailableNicknameDto.builder()
        .requestNickname(requestNickname)
        .isAvailable(isAvailable)
        .build();
  }

  /**
   * User 엔티티를 상세 프로필 조회용 DTO로 변환합니다.
   *
   * @param user 변환할 User 엔티티
   * @return User의 상세 정보를 담은 ReadProfileDto 객체
   */
  public static UserResponseDto.ReadProfileDto toReadProfileDto(
      User user){
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
        .registeredCompanyName(user.getCompanyName())
        .build();
  }
}
