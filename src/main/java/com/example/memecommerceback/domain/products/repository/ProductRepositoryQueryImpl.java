package com.example.memecommerceback.domain.products.repository;

import com.example.memecommerceback.domain.images.entity.QImage;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductSortFields;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import com.example.memecommerceback.domain.products.entity.QProduct;
import com.example.memecommerceback.domain.users.entity.QUser;
import com.example.memecommerceback.global.exception.PageCustomException;
import com.example.memecommerceback.global.utils.PageUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

  @Override
  public Page<Product> readPageByAll(
      Pageable pageable, List<String> sortList, List<ProductStatus> statusList) {

    QUser qUser = QUser.user;
    QImage qImage = QImage.image;

    Set<String> allowedFields = ProductSortFields.getAllowedFields();

    // 3. 동적 OrderSpecifier 생성
    List<OrderSpecifier<?>> orderSpecifiers
        = PageUtils.createOrderSpecifiers(
            Product.class, qProduct.getMetadata().getName(), sortList, allowedFields);

    JPAQuery<Product> query = jpaQueryFactory
        .selectFrom(qProduct)
        .where(qProduct.status.in(statusList))
        .leftJoin(qProduct.owner, qUser).fetchJoin()
        .leftJoin(qProduct.imageList, qImage).fetchJoin()
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct();

    if (orderSpecifiers != null && !orderSpecifiers.isEmpty()) {
      query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
    }

    List<Product> content = query.fetch();

    long total = jpaQueryFactory
        .selectFrom(qProduct)
        .where(qProduct.status.in(statusList))
        .fetchCount();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public Page<Product> readPageBySeller(
      Pageable pageable, List<String> sortList,
      List<ProductStatus> productStatusList, UUID sellerId) {

    QUser qUser = QUser.user;
    QImage qImage = QImage.image;

    Set<String> allowedFields = ProductSortFields.getAllowedFields();

    // 3. 동적 OrderSpecifier 생성
    List<OrderSpecifier<?>> orderSpecifiers
        = PageUtils.createOrderSpecifiers(
        Product.class, qProduct.getMetadata().getName(), sortList, allowedFields);

    JPAQuery<Product> query = jpaQueryFactory
        .selectFrom(qProduct)
        .where(qProduct.status.in(productStatusList),
            qProduct.owner.id.eq(sellerId))
        .leftJoin(qProduct.owner, qUser).fetchJoin()
        .leftJoin(qProduct.imageList, qImage).fetchJoin()
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct();

    if (orderSpecifiers != null && !orderSpecifiers.isEmpty()) {
      query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
    }

    List<Product> content = query.fetch();

    long total = jpaQueryFactory
        .selectFrom(qProduct)
        .where(qProduct.status.in(productStatusList),
            qProduct.owner.id.eq(sellerId))
        .fetchCount();

    return new PageImpl<>(content, pageable, total);
  }

}