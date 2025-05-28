package com.example.memecommerceback.domain.files.entity;

import com.example.memecommerceback.domain.users.entity.User;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File {

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
  private FileExtension extension;

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User owner;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private FileType fileType;
}
