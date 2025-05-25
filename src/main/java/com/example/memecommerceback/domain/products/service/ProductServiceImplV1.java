package com.example.memecommerceback.domain.products.service;

import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.products.converter.ProductConverter;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.products.dto.ProductTitleDescriptionProjection;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import com.example.memecommerceback.domain.products.exception.ProductCustomException;
import com.example.memecommerceback.domain.products.exception.ProductExceptionCode;
import com.example.memecommerceback.domain.products.repository.ProductRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ResponseDto;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import com.example.memecommerceback.global.utils.DateUtils;
import com.example.memecommerceback.global.utils.PageUtils;
import com.example.memecommerceback.global.utils.RabinKarpUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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

    // 1. 요청한 판매 시작일/마감일 검증
    // 해당 판매 시작일/마감일은 특정 시간만 허용한다.
    // 10:00, 11:00, 12:00, 19:00, 20:00, 21:00
    DateUtils.validateDateTime(requestDto.getSellStartDate(), requestDto.getSellEndDate());

    // 2. 제목/설명에 비속어가 들어갔는지?
    validateProfanityText(requestDto.getName(), requestDto.getDescription(), loginUser);

    // 3. 상품을 만듦.
    Product product = ProductConverter.toEntity(requestDto, loginUser);

    List<S3ResponseDto> uploadedImages = null;
    List<Image> imageList = null;

    try {
      // 1. S3 업로드 먼저 (트랜잭션 외부)
      uploadedImages = imageService.uploadProductImageList(
          productImageList, loginUser.getNickname());

      // 2. DB 작업들 (트랜잭션 내부 - 실패 시 롤백)
      imageList = imageService.toEntityListAndSaveAll(uploadedImages, loginUser);

      product.addImageList(imageList);
      productRepository.save(product);

    } catch (Exception e) {
      // 3. S3 업로드는 성공했지만 DB 작업 실패 시 S3 정리
      if (uploadedImages != null && !uploadedImages.isEmpty()) {
        for (S3ResponseDto dto : uploadedImages) {
          try {
            imageService.deleteS3Object(dto.getUrl());
          } catch (Exception cleanupEx) {
            log.warn("S3 보상 삭제 실패: {}", dto.getUrl(), cleanupEx);
          }
        }
      }
      throw e;
    }

    return ProductConverter.toRegisterOneDto(product, loginUser.getName(), imageList);
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

    if (product.getStatus().equals(afterStatus)) {
      throw new ProductCustomException(ProductExceptionCode.REQUEST_SAME_STATUS);
    }

    if (!beforeStatus.canSellerChangeTo(afterStatus)) {
      throw new ProductCustomException(ProductExceptionCode.CANNOT_MODIFY_STATUS);
    }

    validateProfanityText(requestDto.getName(), requestDto.getDescription(), seller);

    // TODO : stock 변경 시, 재고락 서비스에서 해당 재고 업데이트 로직 필요
    if (afterStatus == ProductStatus.RESALE_SOON || afterStatus == ProductStatus.ON_SALE) {
      if (requestDto.getSellStartDate() == null || requestDto.getSellEndDate() == null) {
        throw new ProductCustomException(ProductExceptionCode.NEED_TO_SELL_DATE);
      }
      DateUtils.validateDateTime(requestDto.getSellStartDate(), requestDto.getSellEndDate());
      product.update(
          afterStatus, requestDto.getSellStartDate(), requestDto.getSellEndDate(),
          requestDto.getName(), requestDto.getDescription(),
          requestDto.getPrice(), requestDto.getStock());
    } else if (afterStatus == ProductStatus.HIDDEN) {
      product.update(
          afterStatus, null, null,
          requestDto.getName(), requestDto.getDescription(),
          requestDto.getPrice(), requestDto.getStock());
    }

    List<S3ResponseDto> uploadedImages = null;
    List<Image> newImageList = null;

    try {
      // 1. 새 이미지 S3 업로드
      uploadedImages = imageService.uploadProductImageList(multipartFileList, seller.getNickname());

      // 2. DB 작업들
      newImageList = imageService.toEntityListAndSaveAll(uploadedImages, seller);

      // 3. 업로드 성공 시에만 기존 이미지 삭제
      imageService.deleteProductImageList(product.getId(), seller.getId());

      // 4. 새 이미지와 상품 연결
      product.addImageList(newImageList);

    } catch (Exception e) {
      // 보상 처리
      if (uploadedImages != null && !uploadedImages.isEmpty()) {
        for (S3ResponseDto dto : uploadedImages) {
          try {
            imageService.deleteS3Object(dto.getUrl());
          } catch (Exception cleanupEx) {
            log.warn("S3 보상 삭제 실패: {}", dto.getUrl(), cleanupEx);
          }
        }
      }
      throw e;
    }

    return ProductConverter.toUpdateOneDto(
        product, seller.getName(), newImageList);
  }

  @Override
  @Transactional
  public ProductResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      UUID productId, String requestedStatus, User admin) {
    Product product = findById(productId);
    ProductStatus status = ProductStatus.fromStatus(requestedStatus);

    if (product.getStatus().equals(status)) {
      throw new ProductCustomException(ProductExceptionCode.REQUEST_SAME_STATUS);
    }

    switch (status) {
      case REJECTED, HIDDEN -> product.updateStatusAndDate(status, null, null);
      case RESALE_SOON, ON_SALE -> {
        DateUtils.validateDateTime(product.getSellStartDate(), product.getSellEndDate());
        product.updateStatus(status);
      }
      case PENDING, TEMP_OUT_OF_STOCK ->
          throw new ProductCustomException(ProductExceptionCode.CANNOT_MODIFY_STATUS);
      default -> throw new ProductCustomException(ProductExceptionCode.UNKNOWN_STATUS);
    }

    return ProductConverter.toUpdateOneStatusDto(product);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponseDto.ReadOneDto readOne(UUID productId) {
    Product product = productRepository.findDetailsById(productId);
    if(product == null){
      throw new ProductCustomException(ProductExceptionCode.NOT_FOUND);
    }
    // TODO : viewCount 로직 Redis로 할 지?, 엔티티에 increaseViewCount()로 구현할지?
    return ProductConverter.toReadOneDto(product);
  }

  // TODO : readPageBy.. 메서드 모두
  //  클라이언트의 요청에 따라 ReadOneDto가 아닌 ReadSummaryOneDto를 넣어서
  //  응답 형태를 더 가볍게 할 예정 (productImageList 또한 마찬가지)
  //  All, Seller, Admin으로 분기
  //  확장성 : 추가 정책이 필요할지도?
  @Override
  @Transactional(readOnly = true)
  public Page<ReadOneDto> readPageByAll(
      int page, int size, List<String> sortList, List<String> statusList) {
    // 1. 정렬 가능한 필드를 요청했는지?
    Pageable pageable = validateSortFieldsAndGetPageable(sortList, page, size);

    // 2. 비로그인/유저 권한으로 특정 상태의 상품 페이지를 못 보도록 막음.
    // 검수 중, 거절된 상품, 숨김 상품을 못 보도록 함.
    List<ProductStatus> readOnlyStatusList
        = ProductStatus.getAndValidateStatusListByUser(statusList);

    // 3. 유효한 정렬 리스트를 통해 페이지로 변환
    Page<Product> productPage
        = productRepository.readPageByAll(pageable, sortList, readOnlyStatusList);
    return ProductConverter.toReadPageDto(productPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductResponseDto.ReadOneDto> readPageBySeller(
      int page, int size, List<String> sortList,
      List<String> statusList, User seller) {
    // 1. 정렬 가능한 필드를 요청했는지?
    Pageable pageable = validateSortFieldsAndGetPageable(sortList, page, size);

    List<ProductStatus> productStatusList
        = ProductStatus.fromStatusList(statusList);
    Page<Product> productPage
        = productRepository.readPageBySeller(pageable, sortList, productStatusList, seller.getId());
    return ProductConverter.toReadPageDto(productPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductResponseDto.ReadOneDto> readPageByAdmin(
      int page, int size, List<String> sortList, List<String> statusList) {
    // 1. 정렬 가능한 필드를 요청했는지?
    Pageable pageable = validateSortFieldsAndGetPageable(sortList, page, size);

    List<ProductStatus> productStatusList
        = ProductStatus.fromStatusList(statusList);
    Page<Product> productPage
        = productRepository.readPageByAll(
            pageable, sortList, productStatusList);
    return ProductConverter.toReadPageDto(productPage);
  }

  // TODO : ProductStatus는 Indexing 고려
  @Override
  @Transactional
  public void updateOnSaleStatus(){
    List<Product> updateStatusList
        = productRepository.findAllByStatusAndSellStartDateBefore(
            ProductStatus.RESALE_SOON, LocalDateTime.now());
    for(Product product : updateStatusList) {
      product.updateStatus(ProductStatus.HIDDEN);
    }
  }

  @Override
  @Transactional
  public void updateHiddenStatus(){
    List<Product> updateStatusList
        = productRepository.findAllBySellEndDateBefore(LocalDateTime.now());
    for(Product product : updateStatusList) {
      product.updateStatusAndDate(
          ProductStatus.HIDDEN, null, null);
    }
  }

  @Override
  @Transactional
  public Product findById(UUID productId) {
    return productRepository.findById(productId).orElseThrow(
        () -> new ProductCustomException(ProductExceptionCode.NOT_FOUND));
  }

  private void validateProfanityText(String newTitle, String newDescription, User user) {
    profanityFilterService.validateListNoProfanity(List.of(newTitle, newDescription));

    List<ProductTitleDescriptionProjection> myProducts = productRepository.findAllByOwner(user);

    for (ProductTitleDescriptionProjection p : myProducts) {
      double titleSimilarity = RabinKarpUtils.slidingWindowSimilarity(
          newTitle, p.getName(), RabinKarpUtils.WINDOW_SIZE);
      if (titleSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
        throw new ProductCustomException(ProductExceptionCode.SIMILAR_PRODUCT_TITLE_EXISTS);
      }

      double descSimilarity = RabinKarpUtils.slidingWindowSimilarity(
          newDescription, p.getDescription(), RabinKarpUtils.WINDOW_SIZE);
      if (descSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
        throw new ProductCustomException(ProductExceptionCode.SIMILAR_PRODUCT_DESCRIPTION_EXISTS);
      }
    }
  }

  private Pageable validateSortFieldsAndGetPageable(
      List<String> sortList, int page, int size){
    PageUtils.validateProductSortFields(sortList);
    return PageRequest.of(page, size);
  }
}