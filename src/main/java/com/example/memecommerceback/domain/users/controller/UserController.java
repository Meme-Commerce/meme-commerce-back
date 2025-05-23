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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  public ResponseEntity<UserResponseDto.UpdateProfileDto> updateProfile(
      @RequestPart(name = "data") @Valid UserRequestDto.UpdateProfileDto requestDto,
      @RequestPart(required = false, name = "image") MultipartFile profileImage,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    UserResponseDto.UpdateProfileDto responseDto
        = userService.updateProfile(requestDto, profileImage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @GetMapping("/users/available-nickname")
  public ResponseEntity<UserResponseDto.IsAvailableNicknameDto> isAvailableNickname(
      @RequestParam @Pattern(
          regexp = "^[a-zA-Z0-9가-힣]{2,20}$",
          message = "닉네임은 2~20자, 영문/숫자/한글만 허용됩니다.")
      String nickname){
    UserResponseDto.IsAvailableNicknameDto responseDto
        = userService.isAvailableNickname(nickname);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @PatchMapping("/users/nickname")
  public ResponseEntity<UserResponseDto.UpdateProfileDto> updateNickname(
      @RequestParam @Pattern(
          regexp = "^[a-zA-Z0-9가-힣]{2,20}$",
          message = "닉네임은 2~20자, 영문/숫자/한글만 허용됩니다.")
      String nickname,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    UserResponseDto.UpdateProfileDto responseDto
        = userService.updateNickname(nickname, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @GetMapping("/users/profile")
  public ResponseEntity<UserResponseDto.ReadProfileDto> readProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    UserResponseDto.ReadProfileDto responseDto
        = userService.readProfile(userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}
