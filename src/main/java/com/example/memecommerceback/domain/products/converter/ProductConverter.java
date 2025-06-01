package com.example.memecommerceback.domain.products.converter;

import com.example.memecommerceback.domain.emoji.converter.EmojiConverter;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.productCategory.converter.ProductCategoryConverter;
import com.example.memecommerceback.domain.productHashtag.converter.ProductHashtagConverter;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

public class ProductConverter {

  public static Product toEntity(
      ProductRequestDto.RegisterOneDto requestDto, User user) {
    return Product.builder()
        .owner(user)
        .name(requestDto.getName())
        .description(requestDto.getDescription())
        .price(requestDto.getPrice())
        .stock(requestDto.getStock())
        .sellStartDate(requestDto.getSellStartDate())
        .sellEndDate(requestDto.getSellEndDate())
        .build();
  }

  public static Product toEntity(
      ProductRequestDto.EmojiPackDto requestDto, User user) {
    return Product.builder()
        .owner(user)
        .name(requestDto.getName())
        .description(requestDto.getDescription())
        .price(requestDto.getPrice())
        .stock(requestDto.getStock())
        .sellStartDate(requestDto.getSellStartDate())
        .sellEndDate(requestDto.getSellEndDate())
        .build();
  }

  public static ProductResponseDto.RegisterOneDto toRegisterOneDto(
      Product product, String ownerName, List<Image> productImageList) {
    return ProductResponseDto.RegisterOneDto.builder()
        .productId(product.getId())
        .createdAt(product.getCreatedAt())
        .name(product.getName())
        .description(product.getDescription())
        .stock(product.getStock())
        .price(product.getPrice())
        .ownerName(ownerName)
        .imageResponseDtoList(ImageConverter.toResponseDtoList(productImageList))
        .productCategoryResponseDtoList(
            ProductCategoryConverter.toResponseDtoList(product.getProductCategoryList()))
        .productHashtagResponseDtoList(
            ProductHashtagConverter.toResponseDtoList(product.getProductHashtagList()))
        .status(product.getStatus())
        .sellStartDate(product.getSellStartDate())
        .sellEndDate(product.getSellEndDate())
        .build();
  }

  public static ProductResponseDto.UpdateOneStatusDto toUpdateOneStatusDto(
      Product product) {
    return ProductResponseDto.UpdateOneStatusDto.builder()
        .productId(product.getId())
        .name(product.getName())
        .status(product.getStatus())
        .modifiedAt(product.getModifiedAt())
        .createdAt(product.getCreatedAt())
        .build();
  }

  public static ProductResponseDto.UpdateOneDto toUpdateOneDto(
      Product product, String ownerName, List<Image> productImageList) {
    return ProductResponseDto.UpdateOneDto.builder()
        .productId(product.getId())
        .createdAt(product.getCreatedAt())
        .name(product.getName())
        .description(product.getDescription())
        .stock(product.getStock())
        .price(product.getPrice())
        .ownerName(ownerName)
        .imageResponseDtoList(ImageConverter.toResponseDtoList(productImageList))
        .status(product.getStatus())
        .build();
  }

  public static ProductResponseDto.ReadOneDto toReadOneDto(
      Product product) {
    return ProductResponseDto.ReadOneDto.builder()
        .productId(product.getId())
        .createdAt(product.getCreatedAt())
        .name(product.getName())
        .description(product.getDescription())
        .stock(product.getStock())
        .price(product.getPrice())
        .ownerName(product.getOwner().getName())
        .imageResponseDtoList(
            ImageConverter.toResponseDtoList(product.getImageList()))
        .status(product.getStatus())
        .likeCount(product.getLikeCount())
        .viewCount(product.getViewCount())
        .build();
  }

  public static Page<ProductResponseDto.ReadOneDto> toReadPageDto(
      Page<Product> productPage) {
    return productPage.map(ProductConverter::toReadOneDto);
  }

  public static ProductResponseDto.EmojiPackDto toEmojiPackDto(
      Product product, String ownerName, List<Image> mainProductImageList,
      List<Emoji> emojiList) {
    return ProductResponseDto.EmojiPackDto.builder()
        .productId(product.getId())
        .createdAt(product.getCreatedAt())
        .name(product.getName())
        .description(product.getDescription())
        .stock(product.getStock())
        .price(product.getPrice())
        .ownerName(ownerName)
        .mainImageResponseDtoList(ImageConverter.toResponseDtoList(mainProductImageList))
        .productCategoryResponseDtoList(
            ProductCategoryConverter.toResponseDtoList(product.getProductCategoryList()))
        .productHashtagResponseDtoList(
            ProductHashtagConverter.toResponseDtoList(product.getProductHashtagList()))
        .emojiResponseDtoList(
            EmojiConverter.toResponseDtoList(emojiList))
        .status(product.getStatus())
        .sellStartDate(product.getSellStartDate())
        .sellEndDate(product.getSellEndDate())
        .build();
  }
}
