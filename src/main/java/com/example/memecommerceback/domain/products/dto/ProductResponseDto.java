package com.example.memecommerceback.domain.products.dto;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.images.dto.ImageResponseDto;
import com.example.memecommerceback.domain.productCategory.dto.ProductCategoryResponseDto;
import com.example.memecommerceback.domain.productHashtag.dto.ProductHashtagResponseDto;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductResponseDto.RegisterOneDto",
      description = "상품 하나 등록 응답 DTO")
  public static class RegisterOneDto {

    @Schema(description = "생성된 상품의 식별자", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "상품명", example = "빈티지 램프")
    private String name;

    @Schema(description = "상품 설명", example = "아름다운 앤티크 램프")
    private String description;

    @Schema(description = "상품 가격(원)", example = "15000")
    private BigDecimal price;

    @Schema(description = "상품 소유자명", example = "홍길동")
    private String ownerName;

    @Schema(description = "상품 이미지 리스트", example = "[]")
    private List<ImageResponseDto> imageResponseDtoList;

    @Schema(description = "상품 카테고리 리스트", example = "[전자기기, 생활용품]")
    private List<ProductCategoryResponseDto> productCategoryResponseDtoList;

    @Schema(description = "상품 해시태그 리스트", example = "[편안함, 엔티크]")
    private List<ProductHashtagResponseDto> productHashtagResponseDtoList;

    @Schema(description = "상품 재고 수량", example = "100")
    private Long stock;

    @Schema(description = "등록된 회사명", example = "홍길동상사")
    private String registeredCompanyName;

    @Schema(description = "상품 상태", example = "PENDING")
    private ProductStatus status;

    @Schema(description = "생성 시각", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "상품 판매 시작일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-20T10:00:00")
    private LocalDateTime sellStartDate;

    @Schema(description = "상품 판매 종료일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-30T23:59:59")
    private LocalDateTime sellEndDate;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductResponseDto.UpdateOneDto",
      description = "상품 하나 수정 응답 DTO")
  public static class UpdateOneDto {

    @Schema(description = "상품 식별자", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "상품명", example = "빈티지 램프")
    private String name;

    @Schema(description = "상품 설명", example = "아름다운 앤티크 램프")
    private String description;

    @Schema(description = "상품 가격(원)", example = "15000")
    private BigDecimal price;

    @Schema(description = "상품 소유자명", example = "홍길동")
    private String ownerName;

    @Schema(description = "상품 이미지 리스트", example = "[]")
    private List<ImageResponseDto> imageResponseDtoList;

    @Schema(description = "상품 재고 수량", example = "100")
    private Long stock;

    @Schema(description = "등록된 회사명", example = "홍길동상사")
    private String registeredCompanyName;

    @Schema(description = "상품 상태", example = "PENDING")
    private ProductStatus status;

    @Schema(description = "생성 시각", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "상품 판매 시작일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-20T10:00:00")
    private LocalDateTime sellStartDate;

    @Schema(description = "상품 판매 종료일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-30T23:59:59")
    private LocalDateTime sellEndDate;

    @Schema(description = "상품 카테고리 리스트", example = "[전자기기, 생활용품]")
    private List<ProductCategoryResponseDto> productCategoryResponseDtoList;

    @Schema(description = "상품 해시태그 리스트", example = "[편안함, 엔티크]")
    private List<ProductHashtagResponseDto> productHashtagResponseDtoList;

  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductResponseDto.UpdateOneStatusDto",
      description = "상품 하나 상태 수정 응답 DTO")
  public static class UpdateOneStatusDto {

    @Schema(description = "상품 식별자", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "상품명", example = "빈티지 램프")
    private String name;

    @Schema(description = "변경된 상품 상태", example = "PENDING")
    private ProductStatus status;

    @Schema(description = "생성 시각", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "최종 수정 시각", example = "2025-05-16T11:20:45")
    private LocalDateTime modifiedAt;

    @Schema(description = "상품 판매 시작일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-20T10:00:00")
    private LocalDateTime sellStartDate;

    @Schema(description = "상품 판매 종료일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-30T23:59:59")
    private LocalDateTime sellEndDate;

    @Schema(description = "상품 카테고리 리스트", example = "[전자기기, 생활용품]")
    private List<ProductCategoryResponseDto> productCategoryResponseDtoList;

    @Schema(description = "상품 해시태그 리스트", example = "[편안함, 엔티크]")
    private List<ProductHashtagResponseDto> productHashtagResponseDtoList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductResponseDto.ReadOneDto",
      description = "상품 하나 조회 응답 DTO")
  public static class ReadOneDto {

    @Schema(description = "상품 식별자", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "상품명", example = "빈티지 램프")
    private String name;

    @Schema(description = "상품 설명", example = "아름다운 앤티크 램프")
    private String description;

    @Schema(description = "상품 가격(원)", example = "15000")
    private BigDecimal price;

    @Schema(description = "상품 소유자명", example = "홍길동")
    private String ownerName;

    @Schema(description = "상품 이미지 리스트", example = "[]")
    private List<ImageResponseDto> imageResponseDtoList;

    @Schema(description = "상품 재고 수량", example = "100")
    private Long stock;

    @Schema(description = "상품 좋아요 수", example = "42")
    private Integer likeCount;

    @Schema(description = "상품 조회수", example = "100")
    private Integer viewCount;

    @Schema(description = "상품 상태", example = "PENDING")
    private ProductStatus status;

    @Schema(description = "생성 시각", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "상품 판매 시작일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-20T10:00:00")
    private LocalDateTime sellStartDate;

    @Schema(description = "상품 판매 종료일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-30T23:59:59")
    private LocalDateTime sellEndDate;

    @Schema(description = "상품 카테고리 리스트", example = "[전자기기, 생활용품]")
    private List<ProductCategoryResponseDto> productCategoryResponseDtoList;

    @Schema(description = "상품 해시태그 리스트", example = "[편안함, 엔티크]")
    private List<ProductHashtagResponseDto> productHashtagResponseDtoList;

  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EmojiPackDto {

    @Schema(description = "생성된 상품의 식별자", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID productId;

    @Schema(description = "상품명", example = "빈티지 램프")
    private String name;

    @Schema(description = "상품 설명", example = "아름다운 앤티크 램프")
    private String description;

    @Schema(description = "상품 가격(원)", example = "15000")
    private BigDecimal price;

    @Schema(description = "상품 소유자명", example = "홍길동")
    private String ownerName;

    @Schema(description = "상품 응답 리스트", example = "[]")
    private List<ImageResponseDto> mainImageResponseDtoList;

    @Schema(description = "이모지 응답 리스트", example = "[]")
    private List<EmojiResponseDto> emojiResponseDtoList;

    @Schema(description = "상품 카테고리 응답 리스트", example = "[전자기기, 생활용품]")
    private List<ProductCategoryResponseDto> productCategoryResponseDtoList;

    @Schema(description = "상품 해시태그 응답 리스트", example = "[편안함, 엔티크]")
    private List<ProductHashtagResponseDto> productHashtagResponseDtoList;

    @Schema(description = "상품 재고 수량", example = "100")
    private Long stock;

    @Schema(description = "등록된 회사명", example = "홍길동상사")
    private String registeredCompanyName;

    @Schema(description = "상품 상태", example = "PENDING")
    private ProductStatus status;

    @Schema(description = "생성 시각", example = "2025-05-15T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "상품 판매 시작일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-20T10:00:00")
    private LocalDateTime sellStartDate;

    @Schema(description = "상품 판매 종료일 (ISO 8601 형식의 날짜/시간)", example = "2025-05-30T23:59:59")
    private LocalDateTime sellEndDate;
  }
}