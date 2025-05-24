package com.example.memecommerceback.domain.images.repository;


import com.example.memecommerceback.domain.images.entity.Image;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {
  Optional<Image> findByUserId(UUID userId);

  Optional<Image> findByOwnerNickname(String ownerNickname);

  List<Image> findAllByProductId(UUID productId);
}
