package com.example.memecommerceback.domain.images.repository;


import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.entity.ImageType;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, UUID> {

  Optional<Image> findByUserId(UUID userId);

  Optional<Image> findByOwnerNickname(String ownerNickname);

  List<Image> findAllByProductId(UUID productId);

  List<Image> findAllByOwnerNicknameAndImageTypeNot(
      String ownerNickname, ImageType imageType);

  @Modifying
  @Query("UPDATE Image i SET i.ownerNickname = :afterNickname, i.prefixUrl = :newPrefixUrl " +
      "WHERE i.ownerNickname = :beforeNickname AND i.imageType = :imageType")
  int updateImageOwnerNicknameAndPrefixByType(
      @Param("beforeNickname") String beforeNickname,
      @Param("afterNickname") String afterNickname,
      @Param("imageType") ImageType imageType,
      @Param("newPrefixUrl") String newPrefixUrl
  );
}
