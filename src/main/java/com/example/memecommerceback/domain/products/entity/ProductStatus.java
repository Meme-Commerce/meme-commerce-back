package com.example.memecommerceback.domain.products.entity;

import com.example.memecommerceback.domain.products.exception.ProductCustomException;
import com.example.memecommerceback.domain.products.exception.ProductExceptionCode;
import java.util.List;
import lombok.Getter;

@Getter
public enum ProductStatus {

  TEMP_OUT_OF_STOCK(Status.TEMP_OUT_OF_STOCK),
  HIDDEN(Status.HIDDEN),
  PENDING(Status.PENDING),
  ON_SALE(Status.ON_SALE),
  RESALE_SOON(Status.RESALE_SOON),
  REJECTED(Status.REJECTED);

  private final String status;

  ProductStatus(String status) {
    this.status = status;
  }

  public static class Status {

    // 임시 품절 상태
    public static final String TEMP_OUT_OF_STOCK = "TEMP_OUT_OF_STOCK";
    // 숨김 상태
    public static final String HIDDEN = "HIDDEN";
    // 검수 중
    public static final String PENDING = "PENDING";
    // 판매 중 상태
    public static final String ON_SALE = "ON_SALE";
    // 곧 재판매 예정 상태
    public static final String RESALE_SOON = "RESALE_SOON";
    public static final String REJECTED = "REJECTED";
  }

  public static ProductStatus fromStatus(String status) {
    for (ProductStatus ps : ProductStatus.values()) {
      if (ps.getStatus().equals(status)) {
        return ps;
      }
    }
    throw new ProductCustomException(ProductExceptionCode.UNKNOWN_STATUS);
  }

  public static List<ProductStatus> getStatusList() {
    return List.of(
        ProductStatus.PENDING, ProductStatus.ON_SALE,
        ProductStatus.HIDDEN, ProductStatus.RESALE_SOON, ProductStatus.TEMP_OUT_OF_STOCK);
  }

  public static List<ProductStatus> getAllowedStatusList() {
    return List.of(
        ProductStatus.ON_SALE, ProductStatus.RESALE_SOON,
        ProductStatus.TEMP_OUT_OF_STOCK);
  }
}

