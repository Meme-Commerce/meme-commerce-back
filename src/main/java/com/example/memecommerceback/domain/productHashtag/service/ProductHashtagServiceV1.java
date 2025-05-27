package com.example.memecommerceback.domain.productHashtag.service;

import com.example.memecommerceback.domain.products.entity.Product;
import java.util.List;

public interface ProductHashtagServiceV1 {

  void resetHashtags(Product product, List<Long> hashtagIdList);
}
