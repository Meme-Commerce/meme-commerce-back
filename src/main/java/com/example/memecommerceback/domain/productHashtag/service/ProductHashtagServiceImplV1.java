package com.example.memecommerceback.domain.productHashtag.service;

import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import com.example.memecommerceback.domain.hashtags.service.HashtagServiceV1;
import com.example.memecommerceback.domain.productHashtag.entity.ProductHashtag;
import com.example.memecommerceback.domain.productHashtag.repository.ProductHashtagRepository;
import com.example.memecommerceback.domain.products.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductHashtagServiceImplV1 implements ProductHashtagServiceV1 {

  private final ProductHashtagRepository productHashtagRepository;
  private final HashtagServiceV1 hashtagService;

  @Override
  @Transactional
  public void resetHashtags(Product product, List<Long> hashtagIdList) {
    // 1. 기존 연결 모두 제거 (orphanRemoval = true 필요)
    product.getProductHashtagList().clear();

    // 2. 새로운 연결 추가
    List<Hashtag> hashtags = hashtagService.findAllById(hashtagIdList);
    List<ProductHashtag> newProductHashtags = hashtags.stream()
        .map(hashtag -> ProductHashtag.builder()
            .product(product)
            .hashtag(hashtag)
            .hashtagName(hashtag.getName())
            .build())
        .toList();

    product.getProductHashtagList().addAll(newProductHashtags);
    productHashtagRepository.saveAll(newProductHashtags);
  }
}
