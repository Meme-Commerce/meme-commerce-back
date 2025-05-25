package com.example.memecommerceback.domain.products.repository;

import com.example.memecommerceback.domain.products.entity.Product;
import java.util.UUID;

public interface ProductRepositoryQuery {
  Product findDetailsById(UUID productId);
}
