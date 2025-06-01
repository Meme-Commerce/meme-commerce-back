package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.dto.EmojiThumbnailResponseDto;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto.EmojiDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface EmojiServiceV1 {

  List<Emoji> register(
      List<MultipartFile> emojiImageList, Product product, User seller,
      String emojiPackName, List<EmojiDto> emojiDescriptionList);

  List<Emoji> findAllByProductId(UUID productId);

  EmojiResponseDto updateOne(
      Long emojiId, String name, MultipartFile emojiImage, User seller);

  void deleteOne(Long emojiId, User seller);

  Page<EmojiThumbnailResponseDto> readPage(int page, int size);

  EmojiResponseDto readOne(Long emojiId);
}
