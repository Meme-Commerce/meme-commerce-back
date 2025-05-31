package com.example.memecommerceback.domain.emoji.controller;

import com.example.memecommerceback.domain.emoji.service.EmojiServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmojiController {

  private final EmojiServiceV1 emojiService;

  // Emoji의 상품 도메인으로 변경으로 인하여 생성 로직 제외,
  // 이모지의 수정/삭제 로직만 존재하도록 변경
}
