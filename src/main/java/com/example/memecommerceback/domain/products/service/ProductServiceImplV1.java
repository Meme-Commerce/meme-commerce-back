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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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

    List<Image> imageList = null;
    try {
      // 1. 이미지 업로드
      imageList = imageService.uploadAndRegisterProductImage(productImageList, loginUser);

      // 2. 상품과 이미지 연결
      product.addImageList(imageList);

      // 3. 상품 저장 (실패 가능 지점)
      productRepository.save(product);

      // TODO Hashtag, Category 연동 및 상품 등록 알림 (판매자 -> 관리자)

    } catch (Exception e) {
      // 상품 저장 실패 시 업로드된 이미지들 정리
      if (imageList != null && !imageList.isEmpty()) {
        try {
          // S3에서 이미지 삭제
          for (Image image : imageList) {
            // S3Service의 deleteS3Object 호출 필요
            imageService.deleteS3Object(image.getUrl());
          }
          // DB에서 이미지 삭제
          imageService.deleteAll(imageList);
        } catch (Exception cleanupException) {
          log.error("상품 등록 실패 후 이미지 정리 실패: {}",
              cleanupException.getMessage(), cleanupException);
        }
      }
      throw e; // 원본 예외 재발생하여 트랜잭션 롤백
    }

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

    // 안전한 이미지 교체: 업로드 먼저, 삭제 나중에
    List<Image> newImageList = null;
    try {
      // 1. 새 이미지 먼저 업로드
      newImageList = imageService.uploadAndRegisterProductImage(multipartFileList, seller);

      // 2. 업로드 성공 시에만 기존 이미지 삭제
      imageService.deleteProductImageList(product.getId(), seller.getId());

      // 3. 새 이미지와 상품 연결
      product.addImageList(newImageList);

    } catch (Exception e) {
      if (newImageList != null && !newImageList.isEmpty()) {
        try {
          for (Image image : newImageList) {
            imageService.deleteS3Object(image.getUrl());
          }
          imageService.deleteAll(newImageList);
        } catch (Exception cleanupException) {
          log.error("새 이미지 정리 실패: {}", cleanupException.getMessage(), cleanupException);
        }
      }
      throw e;
    }
    return ProductConverter.toUpdateOneDto(
        product, seller.getName(), newImageList);
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

