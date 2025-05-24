package com.example.memecommerceback.domain.products.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.products.converter.ProductConverter;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.dto.ProductTitleDescriptionProjection;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import com.example.memecommerceback.domain.products.exception.ProductCustomException;
import com.example.memecommerceback.domain.products.exception.ProductExceptionCode;
import com.example.memecommerceback.domain.products.repository.ProductRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import com.example.memecommerceback.global.utils.DateUtils;
import com.example.memecommerceback.global.utils.RabinKarpUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImplV1 implements ProductServiceV1 {

  private final ImageServiceV1 imageService;
  private final ProfanityFilterService profanityFilterService;

  private final ProductRepository productRepository;

  @Override
  @Transactional
  public ProductResponseDto.RegisterOneDto registerOne(
      ProductRequestDto.RegisterOneDto requestDto,
      List<MultipartFile> productImageList, User loginUser) {

    DateUtils.validateDateTime(
        requestDto.getSellStartDate(), requestDto.getSellEndDate());

    validateProfanityText(
        requestDto.getName(), requestDto.getDescription(), loginUser);

    Product product = ProductConverter.toEntity(requestDto, loginUser);

    List<Image> imageList
        = imageService.uploadAndRegisterProductImage(productImageList, loginUser);

    // TODO Hashtag, Category 연동 및 상품 등록 알림 (판매자 -> 관리자)
    product.addImageList(imageList);
    productRepository.save(product);
    return ProductConverter.toRegisterOneDto(
        product, loginUser.getName(), imageList);
  }

  @Override
  @Transactional
  public ProductResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      UUID productId, String requestedStatus, User admin) {
    Product product = findById(productId);
    ProductStatus status = ProductStatus.fromStatus(requestedStatus);

    if(product.getStatus().equals(status)){
      throw new ProductCustomException(ProductExceptionCode.REQUEST_SAME_STATUS);
    }

    switch (status) {
      case REJECTED, HIDDEN ->
        product.updateStatusAndDate(status, null, null);
      case RESALE_SOON, ON_SALE -> {
        // 재검증
        DateUtils.validateDateTime(
            product.getSellStartDate(), product.getSellEndDate());
        product.updateStatus(status);
      }
      case PENDING, TEMP_OUT_OF_STOCK ->
        // 초기 상태 = PENDING
        // TEMP_OUT_OF_STOCK = 재고가 떨어졌을 때만 변경 될 수 있는 상태
        throw new ProductCustomException(
            ProductExceptionCode.CANNOT_MODIFY_STATUS);
      default -> throw new ProductCustomException(
          ProductExceptionCode.UNKNOWN_STATUS);
    }

    // TODO: 관리자가 어떤 상태로 변경했는지에 대한 알림 (관리자 -> 상품 판매자)
    //  거절했다면 어떤 이유로 거절했는지 상세
    return ProductConverter.toUpdateOneStatusDto(product);
  }

  @Override
  @Transactional
  public ProductResponseDto.UpdateOneDto updateOneBySeller(
      UUID productId, ProductRequestDto.UpdateOneDto requestDto,
      List<MultipartFile> multipartFileList, User seller) {
    Product product = findById(productId);

    if (!product.getOwner().getId().equals(seller.getId())) {
      throw new ProductCustomException(ProductExceptionCode.NOT_OWNER);
    }

    ProductStatus beforeStatus = product.getStatus();
    ProductStatus afterStatus = requestDto.getStatus();

    if(product.getStatus().equals(afterStatus)){
      throw new ProductCustomException(ProductExceptionCode.REQUEST_SAME_STATUS);
    }

    if (!beforeStatus.canSellerChangeTo(afterStatus)) {
      throw new ProductCustomException(ProductExceptionCode.CANNOT_MODIFY_STATUS);
    }

    validateProfanityText(
        requestDto.getName(), requestDto.getDescription(), seller);

    if (afterStatus == ProductStatus.RESALE_SOON
        || afterStatus == ProductStatus.ON_SALE) {
      if(requestDto.getSellStartDate() == null || requestDto.getSellEndDate() == null){
        throw new ProductCustomException(ProductExceptionCode.NEED_TO_SELL_DATE);
      }
      DateUtils.validateDateTime(
          requestDto.getSellStartDate(), requestDto.getSellEndDate());
      product.updateStatusAndDate(
          afterStatus, requestDto.getSellStartDate(), requestDto.getSellEndDate());
    } else if (afterStatus == ProductStatus.HIDDEN) {
      product.updateStatusAndDate(
          afterStatus, null, null);
    }

    imageService.deleteProductImageList(product.getId(), seller.getId());
    List<Image> productImageList
        = imageService.uploadAndRegisterProductImage(multipartFileList, seller);
    product.addImageList(productImageList);
    return ProductConverter.toUpdateOneDto(
        product, seller.getName(), productImageList);
  }

  @Override
  @Transactional
  public Product findById(UUID productId){
    return productRepository.findById(productId).orElseThrow(
        () -> new ProductCustomException(ProductExceptionCode.NOT_FOUND));
  }

  private void validateProfanityText(
      String newTitle, String newDescription, User user){
    profanityFilterService.validateListNoProfanity(
        List.of(newTitle, newDescription));

    List<ProductTitleDescriptionProjection> myProducts =
        productRepository.findAllByOwner(user);

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
            ProductExceptionCode.SIMILAR_PRODUCT_DESCRIPTION_EXISTS);
      }
    }
  }
}

