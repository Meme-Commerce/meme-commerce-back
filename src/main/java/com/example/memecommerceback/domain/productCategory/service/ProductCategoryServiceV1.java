package com.example.memecommerceback.domain.productCategory.service;

import com.example.memecommerceback.domain.productCategory.dto.ProductCategoryResponseDto;
import com.example.memecommerceback.domain.productCategory.entity.ProductCategory;
import com.example.memecommerceback.domain.products.entity.Product;
import java.util.List;

public interface ProductCategoryServiceV1 {

  void resetCategories(
      Product product, List<Long> categoryIdList);
}
