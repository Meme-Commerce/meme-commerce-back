package com.example.memecommerceback.domain.productHashtag.entity;

import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product_hashtag")
public class ProductHashtag extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "product_hashtag_seq_generator")
  @SequenceGenerator(
      name = "product_hashtag_seq_generator",
      sequenceName = "product_hashtag_seq",
      allocationSize = 1
  )
  private Long id;

  @Column(nullable = false)
  private String hashtagName;

  @JoinColumn(name = "product_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Product product;

  @JoinColumn(name = "hashtag_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Hashtag hashtag;
}
