package com.example.memecommerceback.domain.meme.controller;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.service.MemeServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
public class MemeController {

  private final MemeServiceV1 memeService;

  @PostMapping("/meme")
  public ResponseEntity<CommonResponseDto<MemeResponseDto.CreateDto>> create(
      @RequestBody @Valid MemeRequestDto.CreateDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeResponseDto.CreateDto responseDto
        = memeService.create(requestDto, userDetails.getUser());
      return ResponseEntity.status(HttpStatus.OK).body(
          new CommonResponseDto<>(responseDto, "밈을 생성하였습니다.",
              HttpStatus.OK.value()));
  }

  @PatchMapping("/admin/meme/{memeId}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<
      CommonResponseDto<MemeResponseDto.UpdateOneStatusDto>> updateOneStatusByAdmin(
      @PathVariable Long memeId,
      @RequestParam @NotNull(message = "승인 여부는 필수 입력란입니다.") boolean isApproved,
      @RequestParam @NotNull(message = "승인 여부 알람 메세지는 필수 입력란입니다.")
      @Size(min = 1, max = 100, message = "최소 1자, 최대 100자 이내로 알림 메세지를 작성해주셔야 합니다.")
      String notificationMessage,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeResponseDto.UpdateOneStatusDto responseDto
        = memeService.updateOneStatusByAdmin(
            memeId, isApproved, notificationMessage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(responseDto, "밈 하나의 상태를 수정 하였습니다.",
            HttpStatus.OK.value()));
  }

  @PatchMapping("/meme/{memeId}")
  public ResponseEntity<
      CommonResponseDto<MemeResponseDto.UpdateOneDto>> updateOne(
      @PathVariable Long memeId,
      @RequestBody @Valid MemeRequestDto.UpdateOneDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    MemeResponseDto.UpdateOneDto responseDto
        = memeService.updateOne(memeId, requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "밈 하나를 수정 하였습니다.",
            HttpStatus.OK.value()));
  }

  @GetMapping("/admin/meme")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<
      CommonResponseDto<Page<MemeResponseDto.ReadOneDto>>> readPageByAdmin(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam int year, @RequestParam int quarter){
    Page<MemeResponseDto.ReadOneDto> responseDtoPage
        = memeService.readPageByAdmin(page, size, year, quarter);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage, "밈 페이지 조회를 하였습니다.",
            HttpStatus.OK.value()));
  }

  @GetMapping("/meme")
  public ResponseEntity<
      CommonResponseDto<Page<MemeResponseDto.ReadSummaryOneDto>>> readSummaryPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam int year, @RequestParam int quarter){
    Page<MemeResponseDto.ReadSummaryOneDto> responseDtoPage
        = memeService.readSummaryPage(page, size, year, quarter);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage, "밈 페이지 조회를 하였습니다.",
            HttpStatus.OK.value()));
  }

  @DeleteMapping("/meme")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<
      CommonResponseDto<Void>> deleteMany(
      @RequestParam @NotNull(message = "삭제할 밈 아이디는 필수 입력란입니다.")
      List<Long> deletedMemeIdList,
      @RequestParam @NotNull(message = "삭제 사유는 필수 입력란입니다.")
      String deletedMessage,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    memeService.deleteMany(
        deletedMemeIdList, deletedMessage, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null, "밈(들)을 삭제하였습니다.",
            HttpStatus.OK.value()));
  }
}

