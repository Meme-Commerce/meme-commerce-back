package com.example.memecommerceback.domain.users.controller;

import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.service.UserServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "User API", description = "회원 API")
public class UserController {

  private final UserServiceV1 userService;

  @PatchMapping(value = "/users/profile", consumes = "multipart/form-data")
  @Operation(summary = "회원의 개인 정보 수정", description = "자기 자신은 개인 정보를 수정할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "회원의 개인 정보 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDto.UpdateProfileDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommonResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommonResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<UserResponseDto.UpdateProfileDto>> updateProfile(
      @RequestPart(name = "data") @Valid UserRequestDto.UpdateProfileDto requestDto,
      @RequestPart(required = false, name = "image") MultipartFile profileImage,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    UserResponseDto.UpdateProfileDto responseDto
        = userService.updateProfile(requestDto, profileImage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "회원의 개인 정보 수정 성공", HttpStatus.OK.value()));
  }

  @GetMapping("/users/available-nickname")
  @Operation(summary = "이용 가능한 닉네임 조회",
      description = "모든 사용자는 이용 가능한 닉네임을 조회할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이용 가능한 닉네임 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDto.IsAvailableNicknameDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<CommonResponseDto<UserResponseDto.IsAvailableNicknameDto>> isAvailableNickname(
      @RequestParam @Pattern(
          regexp = "^[a-zA-Z0-9가-힣]{2,20}$",
          message = "닉네임은 2~20자, 영문/숫자/한글만 허용됩니다.")
      String nickname){
    UserResponseDto.IsAvailableNicknameDto responseDto
        = userService.isAvailableNickname(nickname);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "이용 가능한 회원 닉네임 조회 성공", HttpStatus.OK.value()));
  }

  /**
   * 인증된 사용자의 닉네임을 수정합니다.
   *
   * @param nickname 새로 설정할 닉네임 (2~20자, 영문/숫자/한글만 허용)
   * @return 수정된 프로필 정보를 반환합니다.
   */
  @PatchMapping("/users/nickname")
  public ResponseEntity<CommonResponseDto<UserResponseDto.UpdateProfileDto>> updateNickname(
      @RequestParam @Pattern(
          regexp = "^[a-zA-Z0-9가-힣]{2,20}$",
          message = "닉네임은 2~20자, 영문/숫자/한글만 허용됩니다.")
      String nickname,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    UserResponseDto.UpdateProfileDto responseDto
        = userService.updateNickname(nickname, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "닉네임 수정 성공", HttpStatus.OK.value()));
  }

  /**
   * 인증된 사용자의 프로필 정보를 조회합니다.
   *
   * @param userDetails 인증된 사용자 정보
   * @return 사용자의 프로필 정보를 담은 응답
   */
  @GetMapping("/users/profile")
  public ResponseEntity<CommonResponseDto<UserResponseDto.ReadProfileDto>> readProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    UserResponseDto.ReadProfileDto responseDto
        = userService.readProfile(userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "회원 프로필 조회 성공", HttpStatus.OK.value()));
  }

  /**
   * 지정된 UUID를 가진 사용자를 삭제합니다.
   *
   * @param userId 삭제할 사용자의 UUID
   * @param userDetails 인증된 사용자 정보
   * @return 삭제 성공 메시지를 포함한 응답 객체
   */
  @DeleteMapping("/users/{userId}")
  public ResponseEntity<CommonResponseDto<Void>> deleteOne(
      @PathVariable UUID userId,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    userService.deleteOne(userId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null, "회원 탈퇴 성공", HttpStatus.OK.value()));
  }
}
