package com.example.memecommerceback.domain.products.entity;

import com.example.memecommerceback.domain.products.exception.ProductCustomException;
import com.example.memecommerceback.domain.products.exception.ProductExceptionCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.parameters.P;

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

  private static final Set<ProductStatus> USER_RESTRICTED_STATUSES
      = Set.of(HIDDEN, PENDING, REJECTED);

  public static ProductStatus fromStatus(String status) {
    for (ProductStatus ps : ProductStatus.values()) {
      if (ps.getStatus().equals(status)) {
        return ps;
      }
    }
    throw new ProductCustomException(ProductExceptionCode.UNKNOWN_STATUS);
  }

  // 판매자 상태 전이 정책
  public static final Map<ProductStatus, Set<ProductStatus>>
      // ON_SALE은 해당 시간에 ON_SALE로 자동으로 변경되도록 함.
      // TEMP_OUT_OF_STOCK은 재고가 0이 되면 자동으로 변경되도록 함.
      SELLER_ALLOWED_TRANSITIONS = Map.of(
          HIDDEN, Set.of(RESALE_SOON),
          ON_SALE, Set.of(HIDDEN, RESALE_SOON),
          RESALE_SOON, Set.of(HIDDEN),
          TEMP_OUT_OF_STOCK, Set.of(HIDDEN, RESALE_SOON)
  );

  // 검증 메서드
  public boolean canSellerChangeTo(ProductStatus to) {
    return SELLER_ALLOWED_TRANSITIONS
        .getOrDefault(this, Set.of()).contains(to);
  }

  public static List<ProductStatus> fromStatusList(List<String> statusList){
    return statusList.stream().map(ProductStatus::fromStatus).toList();
  }

  public static List<ProductStatus> getAndValidateStatusListByUser(
      List<String> statusList){
    List<ProductStatus> productStatusList
        = statusList.stream().map(ProductStatus::fromStatus).toList();
    for (ProductStatus productStatus : productStatusList) {
      if(USER_RESTRICTED_STATUSES.contains(productStatus)){
        throw new ProductCustomException(ProductExceptionCode.UNAUTHORIZED_READ);
      }
    }
    return productStatusList;
  }
}

