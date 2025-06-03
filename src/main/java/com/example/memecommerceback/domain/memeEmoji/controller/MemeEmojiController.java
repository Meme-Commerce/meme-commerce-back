package com.example.memecommerceback.domain.memeEmoji.controller;

import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.service.MemeEmojiServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "MemeEmoji API", description = "밈-이모지(밈모지) 관리 API")
public class MemeEmojiController {

  private final MemeEmojiServiceV1 memeEmojiService;

  @Operation(summary = "밈모지 생성", description = "특정 밈/이모지 조합의 밈모지를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈모지 생성 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeEmojiResponseDto.CreateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PostMapping("/meme/{memeId}/emojis/{emojiId}")
  public ResponseEntity<CommonResponseDto<MemeEmojiResponseDto.CreateOneDto>> createOne(
      @PathVariable Long memeId, @PathVariable Long emojiId,
      @RequestParam @Size(min = 1, max = 20, message = "밈모지 이름은 1자에서 20자 내외로 입력하셔야합니다.") String name,
      @RequestParam @Size(min = 1, max = 100, message = "요청 사항은 1자에서 100자 내외로 입력하셔야합니다.") String message,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.CreateOneDto responseDto
        = memeEmojiService.createOne(memeId, emojiId, name, message, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈모지 하나를 생성하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈모지 상태(승인/반려) 수정", description = "관리자가 밈모지의 상태를 승인 또는 반려로 변경합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈모지 상태 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeEmojiResponseDto.UpdateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/admin/meme-emoji/{memeEmojiId}")
  public ResponseEntity<CommonResponseDto<MemeEmojiResponseDto.UpdateOneDto>> updateOneStatusByAdmin(
      @PathVariable Long memeEmojiId,
      @RequestParam boolean isApproved,
      @RequestParam @Size(min = 1, max = 100, message = "결정 사유는 1자에서 100자내로 작성 해주셔야 합니다.") String reason,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.UpdateOneDto responseDto
        = memeEmojiService.updateOneStatusByAdmin(memeEmojiId, isApproved, reason, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈모지 상태를 업데이트하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈모지 수정", description = "밈모지의 이름/요청 메시지를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈모지 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeEmojiResponseDto.UpdateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PatchMapping("/meme-emoji/{memeEmojiId}")
  public ResponseEntity<CommonResponseDto<MemeEmojiResponseDto.UpdateOneDto>> updateOne(
      @PathVariable Long memeEmojiId,
      @RequestParam @Size(min = 1, max = 20, message = "밈모지 이름은 1자에서 20자 내외로 입력하셔야합니다.") String name,
      @RequestParam @Size(min = 1, max = 100, message = "요청 사항은 1자에서 100자 내외로 입력하셔야합니다.") String message,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.UpdateOneDto responseDto
        = memeEmojiService.updateOne(memeEmojiId, name, message, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈모지 하나를 수정하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈모지 페이지 조회", description = "밈모지(밈-이모지 조합) 리스트를 페이지 단위로 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈모지 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeEmojiResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @GetMapping("/meme-emoji")
  public ResponseEntity<CommonResponseDto<Page<MemeEmojiResponseDto.ReadOneDto>>> readPage(
      @RequestParam(defaultValue = "0")
      @Min(value = 0, message = "페이지는 0 이상이어야 합니다.") int page,
      @RequestParam(defaultValue = "20")
      @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
      @Max(value = 50, message = "사이즈는 50 이하여야 합니다.") int size){
    Page<MemeEmojiResponseDto.ReadOneDto> responseDtoPage
        = memeEmojiService.readPage(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDtoPage, "밈모지 페이지 조회를 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈모지 단일 조회", description = "특정 밈모지의 상세 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈모지 단일 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeEmojiResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @GetMapping("/meme-emoji/{memeEmojiId}")
  public ResponseEntity<CommonResponseDto<MemeEmojiResponseDto.ReadOneDto>> readOne(
      @PathVariable Long memeEmojiId,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeEmojiResponseDto.ReadOneDto responseDto
        = memeEmojiService.readOne(memeEmojiId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈모지 하나를 조회 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈모지(여러 개) 삭제", description = "관리자가 밈모지 ID 리스트와 삭제 사유를 전달받아 여러 개를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈모지 삭제 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommonResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈모지",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @DeleteMapping("/meme-emoji")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<CommonResponseDto<Void>> deleteMany(
      @RequestParam @NotNull(message = "삭제할 밈모지 아이디는 필수 입력란입니다.")
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