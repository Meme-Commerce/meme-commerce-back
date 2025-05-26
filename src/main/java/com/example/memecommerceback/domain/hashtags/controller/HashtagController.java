package com.example.memecommerceback.domain.hashtags.controller;

import com.example.memecommerceback.domain.hashtags.dto.HashtagRequestDto;
import com.example.memecommerceback.domain.hashtags.dto.HashtagResponseDto;
import com.example.memecommerceback.domain.hashtags.service.HashtagServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Hashtag API", description = "해시태그 관리 API")
public class HashtagController {

  private final HashtagServiceV1 hashtagService;

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/hashtags")
  @Operation(summary = "해시태그(여러 개) 생성", description = "관리자가 해시태그 이름 리스트를 받아 여러 개를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "해시태그 리스트 저장 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = HashtagResponseDto.CreateDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<HashtagResponseDto.CreateDto>> create(
      @RequestBody @Valid HashtagRequestDto.CreateDto requestDto) {
    HashtagResponseDto.CreateDto responseDto = hashtagService.create(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "해시태그 리스트 저장에 성공하셨습니다.", HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/hashtags/{hashtagId}")
  @Operation(summary = "해시태그 단일 수정", description = "관리자가 해시태그의 이름을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "해시태그 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = HashtagResponseDto.UpdateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 해시태그",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<HashtagResponseDto.UpdateOneDto>> updateOne(
      @PathVariable Long hashtagId,
      @RequestParam @NotNull(message = "해시태그 이름은 필수 입력 값입니다.")
      @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$", message = "한글/영문/숫자 1~20자까지 입력 가능합니다.")
      String name) {
    HashtagResponseDto.UpdateOneDto responseDto
        = hashtagService.updateOne(hashtagId, name);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "해시태그 하나 수정 성공하셨습니다.", HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/hashtags/delete")
  @Operation(summary = "해시태그(여러 개) 삭제", description = "관리자가 해시태그 ID 리스트로 여러 개를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "해시태그 삭제 성공",
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
      @ApiResponse(responseCode = "404", description = "존재하지 않는 해시태그",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<Void>> delete(
      @RequestBody @Valid HashtagRequestDto.DeleteDto requestDto) {
    hashtagService.delete(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(null, "해시태그 리스트를 삭제하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "해신태그 페이지 조회",
      description = "모든 사용자는 해시태그 페이지를 조회할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "해시태그 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = HashtagResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  @GetMapping("/hashtags")
  public ResponseEntity<
      CommonResponseDto<Page<HashtagResponseDto.ReadOneDto>>> readPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Page<HashtagResponseDto.ReadOneDto> responseDto
        = hashtagService.readPage(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "해시태그 페이지를 조회 하였습니다.", HttpStatus.OK.value()));
  }
}

