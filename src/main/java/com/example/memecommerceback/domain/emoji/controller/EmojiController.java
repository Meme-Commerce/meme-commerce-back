package com.example.memecommerceback.domain.emoji.controller;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.dto.EmojiThumbnailResponseDto;
import com.example.memecommerceback.domain.emoji.service.EmojiServiceV1;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Emoji API", description = "이모지 API")
public class EmojiController {

  private final EmojiServiceV1 emojiService;

  // 이모지를 생성하는 것이 아닌, 상품 도메인에서 이모지 팩을 생성하는 것으로 변경

  // 이모지 팩에 등록되어 있는 이모지 하나를 변경
  @PreAuthorize("hasAuthority('ROLE_SELLER')")
  @PatchMapping(value = "/emoji-pack/{emojiId}", consumes = "multipart/form-data")
  @Operation(summary = "이모지 하나 수정",
      description = "판매자는 자신이 등록한 이모지 하나를 수정할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이모지 하나 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = EmojiResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 이모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
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
  @Operation(summary = "이모지 하나 삭제",
      description = "판매자는 자신이 등록한 이모지 하나를 삭제할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이모지 하나 삭제 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommonResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 이모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<Void>> deleteOne(
      @PathVariable Long emojiId,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    emojiService.deleteOne(emojiId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null, "이모지 하나를 삭제하였습니다.",
            HttpStatus.OK.value()));
  }

  @GetMapping("/emojis")
  @Operation(summary = "이모지 페이지 조회", description = "이모지 페이지를 조회할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이모지 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = EmojiThumbnailResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<
      CommonResponseDto<Page<EmojiThumbnailResponseDto>>> readPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size) {
    Page<EmojiThumbnailResponseDto> responseDtoPage
        = emojiService.readPage(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage, "이모지 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @GetMapping("/emojis/{emojiId}")
  @Operation(summary = "이모지 하나 조회", description = "이모지 하나의 세부정보를 조회할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이모지 하나 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = EmojiResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 이모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<EmojiResponseDto>> readOne(
          @PathVariable Long emojiId) {
    EmojiResponseDto responseDto = emojiService.readOne(emojiId);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "이모지 페이지를 조회하였습니다.", HttpStatus.OK.value()));
  }
}
