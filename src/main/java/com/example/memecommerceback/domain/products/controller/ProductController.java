package com.example.memecommerceback.domain.products.controller;

import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.service.ProductServiceV1;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Product API", description = "상품 관리 API")
public class ProductController {

  private final ProductServiceV1 productService;

  @PostMapping(value = "/products", consumes = "multipart/form-data")
  @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
  @Operation(summary = "상품 하나 등록", description = "판매자가 상품 하나를 등록할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 하나 등록 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.RegisterOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<CommonResponseDto<ProductResponseDto.RegisterOneDto>> registerOne(
      @RequestPart(name = "data") @Valid ProductRequestDto.RegisterOneDto requestDto,
      @RequestPart(name = "file-list") List<MultipartFile> productImageList,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ProductResponseDto.RegisterOneDto responseDto =
        productService.registerOne(requestDto, productImageList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "상품 등록하였습니다. 검수 후, 다시 안내 드리겠습니다.",
            HttpStatus.OK.value()));
  }

  @PatchMapping(value = "/admin/products/{productId}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  @Operation(summary = "상품 하나 상태 수정", description = "관리자는 상품 하나의 상태를 수정할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 하나 상태 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.UpdateOneStatusDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "찾을 수 없는 상품",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<ProductResponseDto.UpdateOneStatusDto>> updateOneStatusByAdmin(
      @PathVariable UUID productId, @RequestParam String status,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ProductResponseDto.UpdateOneStatusDto responseDto =
        productService.updateOneStatusByAdmin(productId, status, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "관리자가 상품 상태를 변경하였습니다.",
            HttpStatus.OK.value()));
  }

  @PatchMapping(value = "/products/{productId}", consumes = "multipart/form-data")
  @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
  @Operation(summary = "상품 하나 수정.", description = "판매자는 완료되지 않은 상품을 수정할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 하나 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.UpdateOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "찾을 수 없는 상품",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<ProductResponseDto.UpdateOneDto>> updateOneBySeller(
      @PathVariable UUID productId,
      @RequestPart(name = "data") @Valid ProductRequestDto.UpdateOneDto requestDto,
      @RequestPart(name = "file-list") List<MultipartFile> multipartFileList,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ProductResponseDto.UpdateOneDto responseDto =
        productService.updateOneBySeller(productId, requestDto, multipartFileList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "판매자가 상품 정보를 수정하였습니다.",
            HttpStatus.OK.value()));
  }

  @GetMapping("/products/{productId}")
  @Operation(summary = "상품 하나 상세 조회",
      description = "모든 사용자는 상품 하나를 상세 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 하나 상세 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "찾을 수 없는 상품",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<ProductResponseDto.ReadOneDto>> readOne(
      @PathVariable UUID productId) {
    ProductResponseDto.ReadOneDto responseDto = productService.readOne(productId);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "상품 정보를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @GetMapping("/products")
  @Operation(summary = "상품 페이지 조회",
      description = "모든 사용자는 특정 상태의 상품 페이지를 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "읽을 수 없는 권한",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageByAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      // 예: sortList=createdAt,asc&sortList=price,desc
      @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
      @RequestParam(defaultValue = "ON_SALE") List<String> statusList) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage =
        productService.readPageByAll(page, size, sortList, statusList);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "모든 회원이 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_SELLER')")
  @GetMapping("/seller/products")
  @Operation(summary = "판매자의 상품 페이지 조회",
      description = "판매자는 자기 자신의 상품 페이지를 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "판매자의 상품 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageBySeller(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
      @RequestParam(defaultValue = "ON_SALE") List<String> statusList,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage =
        productService.readPageBySeller(page, size, sortList, statusList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "판매자가 자신의 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping("/admin/products")
  @Operation(summary = "관리자의 상품 페이지 조회",
      description = "관리자는 모든 상태의 상품 페이지를 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 페이지 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),})
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageByAdmin(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
      @RequestParam(defaultValue = "ON_SALE") List<String> statusList) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage =
        productService.readPageByAdmin(page, size, sortList, statusList);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "관리자가 자신의 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
  @PostMapping("/products/delete")
  @Operation(summary = "상품 삭제",
      description = "판매자가 자신의 상품을 삭제 또는 관리자의 상품 삭제할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상품 삭제 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponseDto.ReadOneDto.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 입력 또는 유저 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "403", description = "삭제할 수 없는 권한",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "찾을 수 없는 상품",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))})
  public ResponseEntity<CommonResponseDto<Void>> deleteMany(
      @RequestBody @Valid ProductRequestDto.DeleteDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    productService.deleteMany(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null,
            "상품 삭제 성공하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_SELLER')")
  @PostMapping(value = "/products/emoji-pack", consumes = "multipart/form-data")
  public ResponseEntity<
      CommonResponseDto<ProductResponseDto.RegisterEmojiPackDto>> registerEmojiPack(
          @RequestPart(name = "data") @Valid ProductRequestDto.RegisterEmojiPackDto requestDto,
          @RequestPart(name = "main-product-image-list") List<MultipartFile> mainProductImageList,
          @RequestPart(name = "image-list") List<MultipartFile> emojiImageList,
          @AuthenticationPrincipal UserDetailsImpl userDetails){
    ProductResponseDto.RegisterEmojiPackDto responseDto =
        productService.registerEmojiPack(
            requestDto, mainProductImageList, emojiImageList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "이모지팩(이모지 세트) 상품 등록하였습니다.",
            HttpStatus.OK.value()));
  }
}