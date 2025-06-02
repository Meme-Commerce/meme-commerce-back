package com.example.memecommerceback.domain.memeEmoji.controller;

import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.service.MemeEmojiServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemeEmojiController {

  private final MemeEmojiServiceV1 memeEmojiService;

  @PostMapping("/meme/{memeId}/emojis/{emojiId}")
  public ResponseEntity<
      CommonResponseDto<MemeEmojiResponseDto.CreateOneDto>> createOne(
      @PathVariable Long memeId, @PathVariable Long emojiId,
      @RequestParam @Size(min = 1, max = 100,
          message = "요청 사항은 1자에서 100자 내외로 입력하셔야합니다.") String message,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.CreateOneDto responseDto
        = memeEmojiService.createOne(
            memeId, emojiId, message, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "밈모지 하나를 생성하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/meme-emoji/{memeEmojiId}")
  public ResponseEntity<
      CommonResponseDto<MemeEmojiResponseDto.UpdateOneDto>> updateOneStatusByAdmin(
      @PathVariable Long memeEmojiId,
      @RequestParam boolean isApproved,
      @RequestParam @Size(min = 1, max = 100,
          message = "결정 사유는 1자에서 100자내로 작성 해주셔야 합니다.") String reason,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.UpdateOneDto responseDto
        = memeEmojiService.updateOneStatusByAdmin(
            memeEmojiId, isApproved, reason, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "밈모지 하나를 생성하였습니다.",
            HttpStatus.OK.value()));
  }
}
