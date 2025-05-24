package com.example.memecommerceback.domain.products.service;

import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServiceV1 {
  ProductResponseDto.RegisterOneDto registerOne(
      ProductRequestDto.RegisterOneDto requestDto,
      List<MultipartFile> productImageList, User loginUser);
}
