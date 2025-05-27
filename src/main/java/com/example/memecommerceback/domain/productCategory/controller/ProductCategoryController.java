package com.example.memecommerceback.domain.productCategory.controller;

import com.example.memecommerceback.domain.productCategory.service.ProductCategoryServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductCategoryController {

  private final ProductCategoryServiceV1 productCategoryService;

}
