package com.example.memecommerceback.domain.products.repository;

import com.example.memecommerceback.domain.products.dto.ProductTitleDescriptionProjection;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.QProduct;
import com.example.memecommerceback.domain.users.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryQueryImpl
    extends QuerydslRepositorySupport
    implements ProductRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;
  private final QProduct qProduct = QProduct.product;

  public ProductRepositoryQueryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Product.class);
    this.jpaQueryFactory = jpaQueryFactory;
  }
}
