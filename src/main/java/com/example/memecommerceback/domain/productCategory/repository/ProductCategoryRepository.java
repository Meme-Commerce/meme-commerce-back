package com.example.memecommerceback.domain.productCategory.repository;


import com.example.memecommerceback.domain.productCategory.entity.ProductCategory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

  List<ProductCategory> findAllByProductId(UUID productId);
}
