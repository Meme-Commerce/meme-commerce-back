package com.example.memecommerceback.domain.products.service;

import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServiceV1 {
  ProductResponseDto.RegisterOneDto registerOne(
      ProductRequestDto.RegisterOneDto requestDto,
      List<MultipartFile> productImageList, User loginUser);

  ProductResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      UUID productId, String requestedStatus, User Admin);

  ProductResponseDto.UpdateOneDto updateOneBySeller(
      UUID productId, ProductRequestDto.UpdateOneDto requestDto,
      List<MultipartFile> multipartFileList, User seller);

  ProductResponseDto.ReadOneDto readOne(UUID productId);

  Page<ReadOneDto> readPage(
      int page, int size, List<String> sortList, List<String> statusList);

  void updateOnSaleStatus();

  void updateHiddenStatus();

  Product findById(UUID productId);
}
