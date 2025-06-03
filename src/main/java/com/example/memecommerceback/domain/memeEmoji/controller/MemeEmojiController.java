package com.example.memecommerceback.domain.memeEmoji.controller;

import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.memeEmoji.service.MemeEmojiServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
      @RequestParam @Size(min = 1, max = 20,
          message = "밈모지 이름은 1자에서 20자 내외로 입력하셔야합니다.") String name,
      @RequestParam @Size(min = 1, max = 100,
          message = "요청 사항은 1자에서 100자 내외로 입력하셔야합니다.") String message,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.CreateOneDto responseDto
        = memeEmojiService.createOne(
            memeId, emojiId, name, message, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "밈모지 하나를 생성하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/admin/meme-emoji/{memeEmojiId}")
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

  @PatchMapping("/meme-emoji/{memeEmojiId}")
  public ResponseEntity<CommonResponseDto<MemeEmojiResponseDto.UpdateOneDto>> updateOne(
      @PathVariable Long memeEmojiId,
      @RequestParam @Size(min = 1, max = 20,
          message = "밈모지 이름은 1자에서 20자 내외로 입력하셔야합니다.") String name,
      @RequestParam @Size(min = 1, max = 100,
          message = "요청 사항은 1자에서 100자 내외로 입력하셔야합니다.") String message,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.UpdateOneDto responseDto
        = memeEmojiService.updateOne(memeEmojiId, name, message, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "밈모지 하나를 수정하였습니다.", HttpStatus.OK.value()));
  }

  @GetMapping("/meme-emoji")
  public ResponseEntity<
      CommonResponseDto<Page<MemeEmojiResponseDto.ReadOneDto>>> readPage(
      @RequestParam(defaultValue = "0")
      @Min(value = 0, message = "페이지는 0 이상이어야 합니다.") int page,
      @RequestParam(defaultValue = "20")
      @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
      @Max(value = 50, message = "사이즈는 50 이하여야 합니다.") int size){
    Page<MemeEmojiResponseDto.ReadOneDto> responseDtoPage
        = memeEmojiService.readPage(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage, "밈모지 페이지 조회를 하였습니다.", HttpStatus.OK.value()));
  }

  @GetMapping("/meme-emoji/{memeEmojiId}")
  public ResponseEntity<
      CommonResponseDto<MemeEmojiResponseDto.ReadOneDto>> readOne(
      @PathVariable Long memeEmojiId,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.ReadOneDto responseDto
        = memeEmojiService.readOne(memeEmojiId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "밈모지 하나를 조회 하였습니다.", HttpStatus.OK.value()));
  }

  @DeleteMapping("/meme-emoji")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<CommonResponseDto<Void>> deleteMany(
      @RequestParam @NotNull(message = "삭제할 밈 아이디는 필수 입력란입니다.")
      @Size(min = 1, max = 10, message = "최소 1개에서 최대 10개까지 삭제 가능합니다.")
      List<Long> deletedMemeEmojiIdList,
      @RequestParam @NotNull(message = "삭제 사유는 필수 입력란입니다.")
      @Size(min = 1, max = 200, message = "최소 1자에서 최대 200자까지 삭제 이유를 적을 수 있습니다.")
      String deletedMessage,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    memeEmojiService.deleteMany(
        deletedMemeEmojiIdList, deletedMessage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(null, "밈모지(들)을 삭제하였습니다.", HttpStatus.OK.value()));
  }
}
