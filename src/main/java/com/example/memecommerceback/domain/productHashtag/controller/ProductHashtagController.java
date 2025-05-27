package com.example.memecommerceback.domain.productHashtag.controller;

import com.example.memecommerceback.domain.productHashtag.service.ProductHashtagServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductHashtagController {

  private final ProductHashtagServiceV1 productHashtagService;
}
