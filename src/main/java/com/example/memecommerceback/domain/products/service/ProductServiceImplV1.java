package com.example.memecommerceback.domain.products.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.products.converter.ProductConverter;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.dto.ProductTitleDescriptionProjection;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.exception.ProductCustomException;
import com.example.memecommerceback.domain.products.exception.ProductExceptionCode;
import com.example.memecommerceback.domain.products.repository.ProductRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.utils.DateUtils;
import com.example.memecommerceback.global.utils.RabinKarpUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImplV1 implements ProductServiceV1 {

  private final ImageServiceV1 imageService;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public ProductResponseDto.RegisterOneDto registerOne(
      ProductRequestDto.RegisterOneDto requestDto,
      List<MultipartFile> productImageList, User loginUser) {

    DateUtils.validateDateTime(
        requestDto.getSellStartDate(), requestDto.getSellEndDate());

    String newTitle = requestDto.getName();
    String newDescription = requestDto.getDescription();

    List<ProductTitleDescriptionProjection> myProducts =
        productRepository.findAllByOwner(loginUser);

    for (ProductTitleDescriptionProjection p : myProducts) {
      double titleSimilarity
          = RabinKarpUtils.slidingWindowSimilarity(
              newTitle, p.getName(), RabinKarpUtils.WINDOW_SIZE);
      if (titleSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
        throw new ProductCustomException(
            ProductExceptionCode.SIMILAR_PRODUCT_TITLE_EXISTS);
      }
      // 설명(description) 유사도 판단
      double descSimilarity = RabinKarpUtils.slidingWindowSimilarity(
          newDescription, p.getDescription(), RabinKarpUtils.WINDOW_SIZE);
      if (descSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
        throw new ProductCustomException(
            ProductExceptionCode.SIMILAR_PRODUCT_TITLE_EXISTS);
      }
    }

    Product product = ProductConverter.toEntity(requestDto, loginUser);

    List<Image> imageList
        = imageService.uploadAndRegisterProductImage(productImageList, loginUser);

    product.addImageList(imageList);
    productRepository.save(product);
    return ProductConverter.toRegisterOneDto(
        product, loginUser.getName(), imageList);
  }
}

