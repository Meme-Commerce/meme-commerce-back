package com.example.memecommerceback.domain.products.controller;

import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.service.ProductServiceV1;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
import io.swagger.v3.oas.annotations.parameters.RequestPart;

@RestController
@Tag(name = "Product", description = "Operations related to Products")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

  private final ProductServiceV1 productService;

  @Operation(
    summary = "Register a new product",
    description = "Creates a new product along with images via multipart/form-data"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "상품 등록 성공",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductResponseDto.RegisterOneDto.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PostMapping(value = "/products", consumes = "multipart/form-data")
  @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
  public ResponseEntity<CommonResponseDto<ProductResponseDto.RegisterOneDto>> registerOne(
    @io.swagger.v3.oas.annotations.parameters.RequestPart(
      name = "data",
      description = "Product data payload",
      required = true,
      content = @Content(schema = @Schema(implementation = ProductRequestDto.RegisterOneDto.class))
    )
    @Valid ProductRequestDto.RegisterOneDto requestDto,
    @io.swagger.v3.oas.annotations.parameters.RequestPart(
      name = "file-list",
      description = "List of product image files",
      required = true,
      content = @Content(mediaType = "multipart/form-data")
    )
    List<MultipartFile> productImageList,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ProductResponseDto.RegisterOneDto responseDto =
        productService.registerOne(requestDto, productImageList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "상품 등록하였습니다. 검수 후, 다시 안내 드리겠습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "Update product status by admin",
    description = "Updates the status of a product by an admin"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "관리자가 상품 상태를 변경하였습니다.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductResponseDto.UpdateOneStatusDto.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PatchMapping(value = "/admin/products/{productId}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<CommonResponseDto<ProductResponseDto.UpdateOneStatusDto>> updateOneStatusByAdmin(
    @PathVariable UUID productId,
    @RequestParam String status,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ProductResponseDto.UpdateOneStatusDto responseDto =
        productService.updateOneStatusByAdmin(productId, status, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "관리자가 상품 상태를 변경하였습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "Update a product by seller",
    description = "Updates product details and images by the seller via multipart/form-data"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "판매자가 상품 정보를 수정하였습니다.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductResponseDto.UpdateOneDto.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PatchMapping(value = "/products/{productId}", consumes = "multipart/form-data")
  @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
  public ResponseEntity<CommonResponseDto<ProductResponseDto.UpdateOneDto>> updateOneBySeller(
    @PathVariable UUID productId,
    @io.swagger.v3.oas.annotations.parameters.RequestPart(
      name = "data",
      description = "Product update payload",
      required = true,
      content = @Content(schema = @Schema(implementation = ProductRequestDto.UpdateOneDto.class))
    )
    @Valid ProductRequestDto.UpdateOneDto requestDto,
    @io.swagger.v3.oas.annotations.parameters.RequestPart(
      name = "file-list",
      description = "List of product image files",
      required = true,
      content = @Content(mediaType = "multipart/form-data")
    )
    List<MultipartFile> multipartFileList,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ProductResponseDto.UpdateOneDto responseDto =
        productService.updateOneBySeller(productId, requestDto, multipartFileList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "판매자가 상품 정보를 수정하였습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "Get product details",
    description = "Retrieves product information by product ID"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "상품 정보를 조회하였습니다.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductResponseDto.ReadOneDto.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @GetMapping("/products/{productId}")
  public ResponseEntity<CommonResponseDto<ProductResponseDto.ReadOneDto>> readOne(
    @PathVariable UUID productId
  ) {
    ProductResponseDto.ReadOneDto responseDto = productService.readOne(productId);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDto,
            "상품 정보를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "List products",
    description = "Retrieves a paginated list of products available to all users"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "모든 회원이 상품 페이지를 조회하였습니다.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = org.springframework.data.domain.Page.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @GetMapping("/products")
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageByAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    // 예: sortList=createdAt,asc&sortList=price,desc
    @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
    @RequestParam(defaultValue = "ON_SALE") List<String> statusList
  ) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage =
        productService.readPageByAll(page, size, sortList, statusList);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "모든 회원이 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "List seller's products",
    description = "Retrieves a paginated list of products for the seller"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "판매자가 자신의 상품 페이지를 조회하였습니다.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = org.springframework.data.domain.Page.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PreAuthorize("hasAuthority('ROLE_SELLER')")
  @GetMapping("/seller/products")
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageBySeller(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    // 예: sortList=createdAt,asc&sortList=price,desc
    @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
    @RequestParam(defaultValue = "ON_SALE") List<String> statusList,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage =
        productService.readPageBySeller(page, size, sortList, statusList, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "판매자가 자신의 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "List all products as admin",
    description = "Retrieves a paginated list of all products for admin"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "관리자가 자신의 상품 페이지를 조회하였습니다.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = org.springframework.data.domain.Page.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping("/admin/products")
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageByAdmin(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    // 예: sortList=createdAt,asc&sortList=price,desc
    @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
    @RequestParam(defaultValue = "ON_SALE") List<String> statusList
  ) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage =
        productService.readPageByAdmin(page, size, sortList, statusList);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "관리자가 자신의 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @Operation(
    summary = "Delete multiple products",
    description = "Deletes multiple products by IDs provided in the request"
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "상품 삭제 성공하였습니다.",
      content = @Content
    ),
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    @ApiResponse(responseCode = "403", description = "권한 거부", content = @Content),
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
  @PostMapping("/products/delete")
  public ResponseEntity<CommonResponseDto<Void>> deleteMany(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Payload for deleting products",
      required = true,
      content = @Content(schema = @Schema(implementation = ProductRequestDto.DeleteDto.class))
    )
    @RequestBody @Valid ProductRequestDto.DeleteDto requestDto,
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    productService.deleteMany(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            null,
            "상품 삭제 성공하였습니다.",
            HttpStatus.OK.value()));
  }
}
            "판매자가 자신의 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping("/admin/products")
  public ResponseEntity<CommonResponseDto<Page<ProductResponseDto.ReadOneDto>>> readPageByAdmin(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt,desc") List<String> sortList,
      @RequestParam(defaultValue = "ON_SALE") List<String> statusList) {
    Page<ProductResponseDto.ReadOneDto> responseDtoPage = productService.readPageByAdmin(
        page, size, sortList, statusList);
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            responseDtoPage,
            "관리자가 자신의 상품 페이지를 조회하였습니다.",
            HttpStatus.OK.value()));
  }

  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
  @PostMapping("/products/delete")
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
}