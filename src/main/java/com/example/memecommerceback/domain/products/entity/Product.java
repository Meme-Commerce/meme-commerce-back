package com.example.memecommerceback.domain.products.entity;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class Product extends CommonEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, length = 200)
  private String description;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Long stock;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus status = ProductStatus.PENDING;

  @Builder.Default
  private Integer viewCount = 0;

  @Builder.Default
  private Integer likeCount = 0;

  @Column(nullable = false)
  private LocalDateTime sellStartDate;

  @Column(nullable = false)
  private LocalDateTime sellEndDate;

  // relations
  @Builder.Default
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> imageList = new ArrayList<>();

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User owner;

  // entity methods
  public void addImageList(List<Image> imageList){
    this.imageList.clear();
    for (Image image : imageList) {
      image.registerProduct(this);
    }
    this.imageList.addAll(imageList);
  }

  public void updateStatus(ProductStatus status){
    this.status = status;
  }

  public void update(
      ProductStatus status, LocalDateTime sellStartDate, LocalDateTime sellEndDate,
      String name, String description, Long price, Long stock){
    this.status = status;
    this.sellStartDate = sellStartDate;
    this.sellEndDate = sellEndDate;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
  }

  public void updateStatusAndDate(
      ProductStatus status, LocalDateTime sellStartDate, LocalDateTime sellEndDate) {
    this.status = status;
    this.sellStartDate = sellStartDate;
    this.sellEndDate = sellEndDate;
  }
}
