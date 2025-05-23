package com.example.memecommerceback.domain.images.entity;

import com.example.memecommerceback.domain.users.entity.User;
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

  // UUID_originalFileName.png
  // ex) UUID_철수.jpeg
  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String originalName;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Extension extension;

  @Column(nullable = false)
  private Integer width;

  @Column(nullable = false)
  private Integer height;

  @Column(nullable = false, unique = true)
  private UUID userId;

  private String ownerNickname;

  public void updateProfile(String ownerNickname, String url){
    this.ownerNickname = ownerNickname;
    this.url = url;
  }
}

