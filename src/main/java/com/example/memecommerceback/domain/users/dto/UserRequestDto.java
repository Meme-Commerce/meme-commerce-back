package com.example.memecommerceback.domain.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(name = "UserRequestDto.UpdateProfileDto",
      description = "회원 개인 정보 수정 요청 DTO")
  public static class UpdateProfileDto {

    @Size(max = 20, message = "닉네임은 20자 이내로 입력해주세요.")
    private String nickname;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@(naver.com|daum.net)$",
        message = "허용된 이메일 도메인은 naver.com 또는 daum.net입니다.")
    private String email;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$",
        message = "연락처는 010-xxxx-xxxx 형식이어야 합니다.")
    private String contact;

    @Pattern(regexp = "^[\\p{L}\\p{N}\\s,.'-]+$",
        message = "주소는 유효한 문자만 포함해야 합니다.")
    private String address;
  }
}
