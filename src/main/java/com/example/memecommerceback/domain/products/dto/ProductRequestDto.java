package com.example.memecommerceback.domain.products.dto;

import com.example.memecommerceback.domain.products.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductRequestDto {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductRequestDto.RegisterOneDto",
          description = "상품 등록 요청 DTO")
  public static class RegisterOneDto {
    @Size(min = 1, max = 30,
        message = "상품명은 최소 1자부터 30자를 입력하셔야합니다.")
    @NotNull(message = "상품 명은 필수 입력란입니다.")
    private String name;
    @Min(value = 1, message = "재고 최소 재고량은 1개입니다.")
    @NotNull(message = "재고는 필수 입력란입니다.")
    private Long stock;
    @Positive(message = "음수 또는 0은 사용할 수 없고 최소 100원 부터 입력 가능합니다.")
    @Min(value = 100, message = "최소 가격은 100원 이상입니다.")
    private Long price;
    @Size(max = 200, message = "설명은 200자까지 입력 가능합니다.")
    @NotNull(message = "설명은 필수 입력란입니다.")
    private String description;
    @NotNull(message = "판매 시작 일은 필수 입력란입니다.")
    private LocalDateTime sellStartDate;
    @NotNull(message = "판매 마감 일은 필수 입력란입니다.")
    private LocalDateTime sellEndDate;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductRequestDto.UpdateOneDto",
      description = "상품 수정 요청 DTO")
  public static class UpdateOneDto {
    @Size(min = 1, max = 30,
        message = "상품명은 최소 1자부터 30자를 입력하셔야합니다.")
    @NotNull(message = "상품 명은 필수 입력란입니다.")
    private String name;
    @Min(value = 1, message = "재고 최소 재고량은 1개입니다.")
    @NotNull(message = "재고는 필수 입력란입니다.")
    private Long stock;
    @Positive(message = "음수 또는 0은 사용할 수 없고 최소 100원 부터 입력 가능합니다.")
    @Min(value = 100, message = "최소 가격은 100원 이상입니다.")
    private Long price;
    @Size(max = 200, message = "설명은 200자까지 입력 가능합니다.")
    @NotNull(message = "설명은 필수 입력란입니다.")
    private String description;
    @NotNull(message = "상품 상태는 필수 입력란입니다.")
    private ProductStatus status;
    // null 가능 -> 상품 상태 RESALE_ON, ON_SALE에 따라 적어야함.
    private LocalDateTime sellStartDate;
    private LocalDateTime sellEndDate;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "ProductRequestDto.DeleteDto",
      description = "상품 삭제 요청 DTO")
  public static class DeleteDto {
    @Size(min = 1, message = "삭제할 상품을 최소 1개 이상 선택해주세요.")
    @NotNull(message = "삭제하려는 아이디 리스트는 필수 입력란입니다.")
    private List<UUID> productIdList;
  }
}
