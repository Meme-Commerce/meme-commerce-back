package com.example.memecommerceback.domain.products.entity;

import com.example.memecommerceback.domain.common.CommonSortFields;
import java.util.Set;
import lombok.Getter;

@Getter
public class ProductSortFields extends CommonSortFields {
  public static final String LIKE_COUNT = "likeCount";
  public static final String VIEW_COUNT = "viewCount";
  public static final String PRICE = "price";

  public static Set<String> getAllowedFields(){
    return Set.of(
        ProductSortFields.LIKE_COUNT, ProductSortFields.VIEW_COUNT,
        ProductSortFields.PRICE, ProductSortFields.CREATED_AT);
  }
}
