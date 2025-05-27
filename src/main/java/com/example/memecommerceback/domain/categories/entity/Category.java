package com.example.memecommerceback.domain.categories.entity;

import com.example.memecommerceback.domain.productCategory.entity.ProductCategory;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "category_seq_generator")
  @SequenceGenerator(
      name = "category_seq_generator",
      sequenceName = "category_seq",
      allocationSize = 1
  )
  private Long id;

  @Column(nullable = false)
  private String name;

  @Builder.Default
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductCategory> productCategoryList = new ArrayList<>();

  public void update(String name){
    this.name = name;
  }
}

