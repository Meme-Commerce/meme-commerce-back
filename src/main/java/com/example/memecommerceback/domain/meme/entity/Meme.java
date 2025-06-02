package com.example.memecommerceback.domain.meme.entity;

import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meme")
public class Meme extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "meme_seq_generator")
  @SequenceGenerator(
      name = "meme_seq_generator", sequenceName = "meme_seq",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String registeredNickname;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MemeStatus status = MemeStatus.PENDING;

  @Column(nullable = false)
  private Integer year;

  @Column(nullable = false)
  private Integer quarter;

  public void updateStatus(boolean isApproved){
    if(isApproved){
      this.status = MemeStatus.APPROVED;
    }else{
      this.status = MemeStatus.REJECTED;
    }
  }

  public void update(String name, String description) {
    if (name != null) this.name = name;
    if (description != null) this.description = description;
  }
}
