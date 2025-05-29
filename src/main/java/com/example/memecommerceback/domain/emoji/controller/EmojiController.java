package com.example.memecommerceback.domain.emoji.controller;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.service.EmojiServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmojiController {

  private final EmojiServiceV1 emojiService;

  // 관리자가 아니라, 권한의 확장 가능성 -> 판매자 (이모지 판매자 / 상품 판매자) 분기 가능성 존재
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/emojis")
  public ResponseEntity<CommonResponseDto<EmojiResponseDto>> createOne(
      @RequestParam(name = "name")
      @NotNull(message = "이모지 이름은 필수 입력란입니다.")
      @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$",
          message = "한글/영문/숫자로 1~20자까지 입력 가능합니다.")
      String name,
      @RequestPart(name = "image") MultipartFile multipartFile,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    EmojiResponseDto responseDto
        = emojiService.createOne(name, multipartFile, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "이모지 등록을 하였습니다.",
            HttpStatus.OK.value()));
  }
}
