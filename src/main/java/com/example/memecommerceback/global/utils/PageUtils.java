package com.example.memecommerceback.global.utils;

import com.example.memecommerceback.domain.common.CommonSortFields;
import com.example.memecommerceback.domain.products.entity.ProductSortFields;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.PageCustomException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageUtils {

  public static final String PRODUCT = "Product";

  public static void validateProductSortFields(List<String> sortList) {
    sortList.forEach(sort -> {
      System.out.println("requested Sort : "+sort);
    });
    if (sortList == null || sortList.isEmpty()) return;

    Set<String> allowedFields = Set.of(
        ProductSortFields.CREATED_AT, ProductSortFields.LIKE_COUNT,
        ProductSortFields.VIEW_COUNT, ProductSortFields.PRICE
    );

    Set<String> invalidFields = new HashSet<>();
    for (String sort : sortList) {
      String field = sort.split(",")[0];
      if (!allowedFields.contains(field)) {
        invalidFields.add(field);
      }
    }

    if (!invalidFields.isEmpty()) {
      throw new PageCustomException(
          GlobalExceptionCode.INVALID_SORT_FIELDS,
          PRODUCT + ": " + String.join(", ", invalidFields)
      );
    }
  }

  public static <T> List<OrderSpecifier<?>> createOrderSpecifiers(
      Class<T> clazz, String alias, List<String> sortList, Set<String> allowedFields) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    PathBuilder<T> entityPath = new PathBuilder<>(clazz, alias);

    for (String sort : sortList) {
      String[] sortArr = sort.split(",");
      String property = sortArr[0].trim();
      String direction = sortArr.length > 1 ? sortArr[1].trim().toLowerCase() : "asc";
      if (!allowedFields.contains(property)) {
        throw new PageCustomException(
            GlobalExceptionCode.INVALID_SORT_FIELDS,
            "[Product] 허용되지 않은 필드: " + property
        );
      }
      Order order = direction.equals("desc") ? Order.DESC : Order.ASC;
      orderSpecifiers.add(new OrderSpecifier(order, entityPath.get(property)));
    }
    return orderSpecifiers;
  }
}
