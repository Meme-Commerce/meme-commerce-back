package com.example.memecommerceback.domain.products.repository;

import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryQuery {

  Product findDetailsById(UUID productId);

  Page<Product> readPageByAll(
      Pageable pageable, List<String> sortList, List<ProductStatus> statusList);

  Page<Product> readPageBySeller(
      Pageable pageable, List<String> sortList,
      List<ProductStatus> productStatusList, UUID sellerId);
}
