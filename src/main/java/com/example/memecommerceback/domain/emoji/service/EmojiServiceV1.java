package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.products.dto.ProductRequestDto.RegisterEmojiDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface EmojiServiceV1 {

  void register(
      List<MultipartFile> emojiImageList, Product product, User seller,
      String emojiPackName, List<RegisterEmojiDto> emojiDescriptionList);
}
