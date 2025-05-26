package com.example.memecommerceback.domain.hashtags.entity;

import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "hashtag")
public class Hashtag extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "hashtag_seq_generator")
  @SequenceGenerator(
      name = "hashtag_seq_generator",
      sequenceName = "hashtag_seq",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false, length = 20)
  private String name;

  public void update(String name){
    this.name = name;
  }
}
