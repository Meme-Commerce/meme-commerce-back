package com.example.memecommerceback.domain.products.controller;

import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.service.ProductServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

  private final ProductServiceV1 productService;

  @PostMapping(value = "/products", consumes = "multipart/form-data")
  @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
  public ResponseEntity<
      CommonResponseDto<ProductResponseDto.RegisterOneDto>> registerOne(
          @RequestPart(name = "data") @Valid ProductRequestDto.RegisterOneDto requestDto,
          @RequestPart(name = "file-list") List<MultipartFile> productImageList,
          @AuthenticationPrincipal UserDetailsImpl userDetails){
    ProductResponseDto.RegisterOneDto responseDto
        = productService.registerOne(
            requestDto, productImageList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "성공적으로 상품 등록하였습니다. 검수 후, 다시 안내 드리겠습니다.",
            HttpStatus.OK.value()));
  }
}
