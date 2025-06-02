package com.example.memecommerceback.domain.meme.controller;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.service.MemeServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

