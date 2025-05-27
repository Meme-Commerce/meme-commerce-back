package com.example.memecommerceback.domain.productCategory.service;

import com.example.memecommerceback.domain.categories.entity.Category;
import com.example.memecommerceback.domain.categories.service.CategoryServiceV1;
import com.example.memecommerceback.domain.productCategory.entity.ProductCategory;
import com.example.memecommerceback.domain.productCategory.repository.ProductCategoryRepository;
import com.example.memecommerceback.domain.products.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImplV1 implements ProductCategoryServiceV1 {

  private final CategoryServiceV1 categoryService;
  private final ProductCategoryRepository productCategoryRepository;

  @Override
  @Transactional
  public void resetCategories(Product product, List<Long> categoryIdList) {
    // 1. 기존 연결 모두 제거 (orphanRemoval = true 필요)
    product.getProductCategoryList().clear();

    // 2. 새로운 연결 추가
    List<Category> categories = categoryService.findAllById(categoryIdList);

    List<ProductCategory> newProductCategories = categories.stream()
        .map(category -> ProductCategory.builder()
            .product(product)
            .category(category)
            .categoryName(category.getName())
            .build())
        .toList();

    product.getProductCategoryList().addAll(newProductCategories);
    productCategoryRepository.saveAll(newProductCategories);
  }

}
