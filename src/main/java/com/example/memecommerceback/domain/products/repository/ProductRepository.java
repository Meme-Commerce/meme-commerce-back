package com.example.memecommerceback.domain.products.repository;


import com.example.memecommerceback.domain.products.dto.ProductTitleDescriptionProjection;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import com.example.memecommerceback.domain.users.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryQuery {

  List<ProductTitleDescriptionProjection> findAllByOwner(User owner);

  List<Product> findAllByStatusAndSellStartDateBefore(
      ProductStatus status, LocalDateTime now);

  List<Product> findAllBySellEndDateBefore(LocalDateTime now);

  List<Product> findAllByOwnerId(UUID ownerId);

  List<Product> findAllByIdInAndStatus(List<UUID> productIdList, ProductStatus status);
}
