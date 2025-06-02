package com.example.memecommerceback.domain.meme.controller;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.service.MemeServiceV1;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Meme API", description = "밈 관리 API")
public class MemeController {

  private final MemeServiceV1 memeService;

  @Operation(summary = "밈 생성", description = "밈을 하나 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈 생성 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeResponseDto.CreateDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PostMapping("/meme")
  public ResponseEntity<CommonResponseDto<MemeResponseDto.CreateDto>> create(
      @RequestBody @Valid MemeRequestDto.CreateDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeResponseDto.CreateDto responseDto
        = memeService.create(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈을 생성하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈 단일 상태(승인/반려) 수정", description = "관리자가 밈의 상태를 승인/반려로 변경합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈 상태 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeResponseDto.UpdateOneStatusDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PatchMapping("/admin/meme/{memeId}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<CommonResponseDto<MemeResponseDto.UpdateOneStatusDto>> updateOneStatusByAdmin(
      @PathVariable Long memeId,
      @RequestParam @NotNull(message = "승인 여부는 필수 입력란입니다.") boolean isApproved,
      @RequestParam @NotNull(message = "승인 여부 알람 메세지는 필수 입력란입니다.")
      @Size(min = 1, max = 100, message = "최소 1자, 최대 100자 이내로 알림 메세지를 작성해주셔야 합니다.")
      String notificationMessage,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeResponseDto.UpdateOneStatusDto responseDto
        = memeService.updateOneStatusByAdmin(memeId, isApproved, notificationMessage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈 하나의 상태를 수정 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈 단일 수정", description = "사용자가 자신의 밈 내용을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeResponseDto.UpdateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @PatchMapping("/meme/{memeId}")
  public ResponseEntity<CommonResponseDto<MemeResponseDto.UpdateOneDto>> updateOne(
      @PathVariable Long memeId,
      @RequestBody @Valid MemeRequestDto.UpdateOneDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeResponseDto.UpdateOneDto responseDto
        = memeService.updateOne(memeId, requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈 하나를 수정 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈 페이지(관리자) 조회", description = "관리자가 연도/분기로 밈 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "권한 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @GetMapping("/admin/meme")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<CommonResponseDto<Page<MemeResponseDto.ReadOneDto>>> readPageByAdmin(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam @Min(value = 2020, message = "최소 2020년부터 조회가 가능합니다.") int year,
      @RequestParam @Min(value = 1, message = "분기는 1-4 사이의 값이어야 합니다.")
      @Max(value = 4, message = "분기는 1-4 사이의 값이어야 합니다.") int quarter){
    Page<MemeResponseDto.ReadOneDto> responseDtoPage
        = memeService.readPageByAdmin(page, size, year, quarter);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDtoPage, "밈 페이지 조회를 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈 페이지(요약) 조회", description = "모든 사용자가 연도/분기로 밈 요약 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈 요약 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemeResponseDto.ReadSummaryOneDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @GetMapping("/meme")
  public ResponseEntity<CommonResponseDto<Page<MemeResponseDto.ReadSummaryOneDto>>> readSummaryPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam @Min(value = 2020, message = "최소 2020년부터 조회가 가능합니다.") int year,
      @RequestParam @Min(value = 1, message = "분기는 1-4 사이의 값이어야 합니다.")
      @Max(value = 4, message = "분기는 1-4 사이의 값이어야 합니다.") int quarter){
    Page<MemeResponseDto.ReadSummaryOneDto> responseDtoPage
        = memeService.readSummaryPage(page, size, year, quarter);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDtoPage, "밈 페이지 조회를 하였습니다.", HttpStatus.OK.value()));
  }

  @Operation(summary = "밈(여러 개) 삭제", description = "관리자가 밈 ID 리스트와 삭제 사유를 전달받아 여러 개를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "밈 삭제 성공",
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
      @ApiResponse(responseCode = "404", description = "존재하지 않는 밈",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  @DeleteMapping("/meme")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<CommonResponseDto<Void>> deleteMany(
      @RequestParam @NotNull(message = "삭제할 밈 아이디는 필수 입력란입니다.")
      @Size(min = 1, max = 10, message = "최소 1개에서 최대 10개까지 삭제 가능합니다.")
      List<Long> deletedMemeIdList,
      @RequestParam @NotNull(message = "삭제 사유는 필수 입력란입니다.")
      @Size(min = 1, max = 200, message = "최소 1자에서 최대 200자까지 삭제 이유를 적을 수 있습니다.")
      String deletedMessage,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    memeService.deleteMany(
        deletedMemeIdList, deletedMessage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(null, "밈(들)을 삭제하였습니다.", HttpStatus.OK.value()));
  }
}

