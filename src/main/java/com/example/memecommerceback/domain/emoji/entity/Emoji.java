package com.example.memecommerceback.domain.emoji.entity;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "emoji")
public class Emoji extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "emoji_seq_generator")
  @SequenceGenerator(
      name = "emoji_seq_generator",
      sequenceName = "emoji_seq",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToOne(fetch = FetchType.LAZY)
  private Image image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @JoinColumn(name = "product_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Product product;

  public void addProduct(Product product) {
    this.product = product;
  }

  public void addProductAndImage(Product product, Image image){
    this.product = product;
    this.image = image;
  }

  public void update(String name){
    this.name = name;
  }

  public void updateImage(Image image){
    this.image = image;
  }
  public void updateNameAndImage(String name, Image image){
    this.name = name;
    this.image = image;
  }
}
