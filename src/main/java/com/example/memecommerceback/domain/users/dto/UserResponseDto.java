package com.example.memecommerceback.domain.users.dto;

import com.example.memecommerceback.domain.users.entity.Gender;
import com.example.memecommerceback.domain.users.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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
    @Schema(description = "사용자 ID", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID userId;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "닉네임", example = "길동이123")
    private String nickname;
    @Schema(description = "나이", example = "28")
    private Integer age;
    @Schema(description = "프로필 이미지 URL",
        example = "https://meme-commerce-bucket.s3.ap-northeast-2.amazonaws.com/users/dbnickname/profile/randomuuid_filename.png")
    private String profileImage;
    @Schema(description = "이메일", example = "hong@example.com")
    private String email;
    @Schema(description = "성별", example = "MALE")
    private Gender gender;
    @Schema(description = "사용자 역할", example = "USER")
    private UserRole userRole;
    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;
    @Schema(description = "생년월일", example = "1995-05-15")
    private String birthDate;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "UserResponseDto.IsAvailableNicknameDto",
      description = "닉네임 사용 가능 여부 응답 DTO")
  public static class IsAvailableNicknameDto {
    @Schema(description = "요청한 닉네임", example = "길동이")
    private String requestNickname;
    @Schema(description = "닉네임 사용 가능 여부", example = "true")
    private boolean isAvailable;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ReadProfileDto {
    @Schema(description = "사용자 ID", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID userId;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "닉네임", example = "길동이123")
    private String nickname;
    @Schema(description = "나이", example = "28")
    private Integer age;
    @Schema(description = "프로필 이미지 URL",
        example = "https://meme-commerce-bucket.s3.ap-northeast-2.amazonaws.com/users/dbnickname/profile/randomuuid_filename.png")
    private String profileImage;
    @Schema(description = "이메일", example = "hong@example.com")
    private String email;
    @Schema(description = "성별", example = "MALE")
    private Gender gender;
    @Schema(description = "사용자 역할", example = "USER")
    private UserRole userRole;
    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;
    @Schema(description = "생년월일", example = "1995-05-15")
    private String birthDate;
    @Schema(description = "생성날짜", example = "2025-05-15")
    private LocalDateTime createdAt;
    @Schema(description = "등록된 회사 이름(특이사항 : 판매자만 등록 가능)", example = "밈커머스")
    private String registeredCompanyName;
  }
}
