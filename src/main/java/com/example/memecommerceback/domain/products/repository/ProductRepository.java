package com.example.memecommerceback.domain.products.repository;


import com.example.memecommerceback.domain.products.dto.ProductTitleDescriptionProjection;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> ,ProductRepositoryQuery {
  List<ProductTitleDescriptionProjection> findAllByOwner(User owner);

}
