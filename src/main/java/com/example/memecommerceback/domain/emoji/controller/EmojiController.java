package com.example.memecommerceback.domain.emoji.controller;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.service.EmojiServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class EmojiController {

  private final EmojiServiceV1 emojiService;

  // 이모지를 생성하는 것이 아닌, 상품 도메인에서 이모지 팩을 생성하는 것으로 변경

  // 이모지 팩에 등록되어 있는 이모지 하나를 변경
  @PreAuthorize("hasAuthority('ROLE_SELLER')")
  @PatchMapping(value = "/emoji-pack/{emojiId}", consumes = "multipart/form-data")
  public ResponseEntity<CommonResponseDto<EmojiResponseDto>> updateOne(
      @PathVariable Long emojiId,
      @RequestParam(required = false) @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$",
          message = "한글, 영어, 숫자로 1~20자까지 입력 가능합니다.") String name,
      @RequestPart(name = "emoji-image", required = false) MultipartFile emojiImage,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    EmojiResponseDto responseDto
        = emojiService.updateOne(emojiId, name, emojiImage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "이모지 하나를 변경하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_SELLER')")
  @DeleteMapping(value = "/emoji-pack/{emojiId}")
  public ResponseEntity<CommonResponseDto<EmojiResponseDto>> deleteOne(
      @PathVariable Long emojiId,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    emojiService.deleteOne(emojiId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null, "이모지 하나를 삭제하였습니다.",
            HttpStatus.OK.value()));
  }
}
