package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.emoji.converter.EmojiConverter;
import com.example.memecommerceback.domain.emoji.dto.EmojiRequestDto;
import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.emoji.excpetion.EmojiCustomException;
import com.example.memecommerceback.domain.emoji.excpetion.EmojiExceptionCode;
import com.example.memecommerceback.domain.emoji.repository.EmojiRepository;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto.RegisterEmojiDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmojiServiceImplV1 implements EmojiServiceV1 {

  private final ImageServiceV1 imageService;
  private final ProfanityFilterService profanityFilterService;

  private final EmojiRepository emojiRepository;

  @Override
  @Transactional
  public void register(
      List<MultipartFile> emojiImageList,  Product product, User seller,
      String emojiPackName, List<RegisterEmojiDto> emojiDescriptionList){
    List<Image> imageList
        = imageService.uploadEmojiImage(emojiImageList, seller, emojiPackName);
    List<Emoji> emojiList
        = EmojiConverter.toEntityList(
            imageList, emojiDescriptionList, product, seller);
    emojiRepository.saveAll(emojiList);
    emojiList.forEach(emoji-> emoji.addProduct(product));
  }
}
