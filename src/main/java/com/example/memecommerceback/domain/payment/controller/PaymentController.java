package com.example.memecommerceback.domain.payment.controller;

import com.example.memecommerceback.domain.payment.dto.PaymentRequestDto;
import com.example.memecommerceback.domain.payment.dto.PaymentResponseDto;
import com.example.memecommerceback.domain.payment.service.PaymentServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Payment API", description = "결제 API")
public class PaymentController {

  private final PaymentServiceV1 paymentService;

  // 토스 페이먼츠 결제 승인
  @PostMapping("/payments/confirm")
  @Operation(summary = "결제 승인 요청",
      description = "상품 주문의 결제를 할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "결제 승인 요청 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PaymentResponseDto.ConfirmOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<
      CommonResponseDto<PaymentResponseDto.ConfirmOneDto>> confirmOne(
      @RequestBody @Valid PaymentRequestDto.ConfirmOneDto requestDto) {
    PaymentResponseDto.ConfirmOneDto responseDto = paymentService.confirmOne(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "토스 페이먼츠를 통해 결제 하였습니다.", HttpStatus.OK.value()));
  }

  // 결제 키를 통한 결제 하나 조회
  @GetMapping("/payments/{paymentKey}")
  @Operation(summary = "결제 하나 조회 요청",
      description = "상품 주문의 결제한 기록을 조회 할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "결제 하나 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PaymentResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommonResponseDto.class))),})
  public ResponseEntity<
      CommonResponseDto<PaymentResponseDto.ReadOneDto>> readOne(
      @PathVariable String paymentKey) {
    PaymentResponseDto.ReadOneDto responseDto = paymentService.readOne(paymentKey);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "결제 하나 조회를 하였습니다.",
            HttpStatus.OK.value()));
  }

  // 결제 취소
  @PostMapping("/payments/{paymentKey}/cancel")
  @Operation(summary = "결제 하나 취소 요청",
      description = "상품 주문의 결제한 기록을 취소 할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "결제 하나 취소 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PaymentResponseDto.CancelOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<CommonResponseDto<
      PaymentResponseDto.CancelOneDto>> cancelOne(
      @RequestBody @Valid PaymentRequestDto.CancelOneDto requestDto) {
    PaymentResponseDto.CancelOneDto responseDto
        = paymentService.cancelOne(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto, "결제 하나 취소를 하였습니다.",
            HttpStatus.OK.value()));
  }
}
