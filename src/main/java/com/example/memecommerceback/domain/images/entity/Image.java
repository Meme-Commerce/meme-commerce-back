package com.example.memecommerceback.domain.images.entity;

import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.global.awsS3.utils.S3Utils;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends CommonEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  // UUID_originalFileName.png
  // ex) UUID_철수.jpeg
  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String originalName;

  @Column(nullable = false)
  private String prefixUrl;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ImageExtension extension;

  @Column(nullable = false)
  private Integer width;

  @Column(nullable = false)
  private Integer height;

  @Column(nullable = false)
  private UUID userId;

  private String ownerNickname;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ImageType imageType;

  @JoinColumn(name = "product_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Product product;

  public void updateProfile(
      String ownerNickname, String prefixUrl, String fileName) {
    this.ownerNickname = ownerNickname;
    this.prefixUrl = prefixUrl;
    this.fileName = fileName;
  }

  public void updateImage(String prefixUrl, String fileName) {
    this.prefixUrl = prefixUrl;
    this.fileName = fileName;
  }

  public void registerProduct(Product product) {
    this.product = product;
  }

  public String getUrl() {
    return this.prefixUrl + this.fileName;
  }

  public String getS3FullUrl(){
    return S3Utils.S3_URL+this.getUrl();
  }
  public void updateOwnerNicknameAndPrefix(String ownerNickname, String prefixUrl) {
    this.ownerNickname = ownerNickname;
    this.prefixUrl = prefixUrl;
  }
}

