package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.emoji.converter.EmojiConverter;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
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
  public List<Emoji> register(
      List<MultipartFile> emojiImageList, Product product, User seller,
      String emojiPackName, List<RegisterEmojiDto> emojiDescriptionList) {
    // 1. 이모지 설명 욕설 존재하는지 확인
    profanityFilterService.validateListNoProfanity(
        emojiDescriptionList.stream().map(RegisterEmojiDto::getDescription).toList());

    // 2. 이모지 설명에 대한 이미지 업로드
    List<Image> imageList
        = imageService.uploadEmojiImage(emojiImageList, seller, emojiPackName);

    // 3. 이모지 리스트 생성 및 반환
    List<Emoji> emojiList
        = EmojiConverter.toEntityList(
        imageList, emojiDescriptionList, product, seller);
    emojiRepository.saveAll(emojiList);

    // 4. 이모지 연관관계 설정
    for (int i = 0; i < emojiList.size(); i++) {
      emojiList.get(i).addProductAndImage(product, imageList.get(i));
    }

    return emojiList;
  }
}
