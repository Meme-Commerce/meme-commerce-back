package com.example.memecommerceback.domain.images.entity;

import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image extends CommonEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String fileName;           // ex: user123_profile.png

  @Column(nullable = false)
  private String originalName;       // ex: IMG_1234.PNG

  @Column(nullable = false)
  private String url;                // S3 or CDN 경로

  @Column(nullable = false)
  private Long size;                 // 바이트 단위

  @Column(nullable = false)
  private String contentType;        // image/png, image/jpeg 등

  @Column(nullable = false)
  private Integer width;

  @Column(nullable = false)
  private Integer height;

  @PrePersist
  public void generateId() {
    if (id == null) {
      this.id = UUID.randomUUID();
    }
  }
}

