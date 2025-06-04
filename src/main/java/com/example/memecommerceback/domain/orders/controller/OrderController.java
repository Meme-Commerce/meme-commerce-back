package com.example.memecommerceback.domain.orders.controller;

import com.example.memecommerceback.domain.orders.dto.OrderRequestDto;
import com.example.memecommerceback.domain.orders.dto.OrderResponseDto;
import com.example.memecommerceback.domain.orders.service.OrderServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
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
public class OrderController {

  private final OrderServiceV1 orderService;

  @PostMapping("/orders")
  public ResponseEntity<
      CommonResponseDto<OrderResponseDto.CreateOneDto>> createOne(
      @RequestBody OrderRequestDto.CreateOneDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    OrderResponseDto.CreateOneDto responseDto
        = orderService.createOne(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "주문을 하나 생성하였습니다.", HttpStatus.OK.value()));
  }
}

