package com.example.memecommerceback.domain.products.repository;

import com.example.memecommerceback.domain.images.entity.QImage;
import com.example.memecommerceback.domain.images.repository.ImageRepository;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.QProduct;
import com.example.memecommerceback.domain.users.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
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

  @Override
  public Product findDetailsById(UUID productId) {
    QUser qUser = QUser.user;
    QImage qImage = QImage.image;

    return jpaQueryFactory.selectFrom(qProduct)
        .where(qProduct.id.eq(productId))
        .leftJoin(qProduct.owner, qUser).fetchJoin()
        .leftJoin(qProduct.imageList, qImage).fetchJoin()
        .distinct()
        .fetchOne();
  }
}
