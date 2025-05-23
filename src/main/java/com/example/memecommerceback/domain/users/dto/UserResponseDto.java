package com.example.memecommerceback.domain.users.dto;

import com.example.memecommerceback.domain.users.entity.Gender;
import com.example.memecommerceback.domain.users.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(name = "UserResponseDto.UpdateProfileDto",
      description = "회원 개인 정보 수정 응답 DTO")
  public static class UpdateProfileDto {
    private UUID userId;
    private String name;
    private String nickname;
    private Integer age;
    private String profileImage;
    private String email;
    private Gender gender;
    private UserRole userRole;
    private String contact;
    private String address;
    private String birthDate;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class IsAvailableNicknameDto {
    private String requestNickname;
    private boolean isAvailable;
  }
}
