package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.emoji.converter.EmojiConverter;
import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.dto.EmojiThumbnailResponseDto;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.emoji.exception.EmojiCustomException;
import com.example.memecommerceback.domain.emoji.exception.EmojiExceptionCode;
import com.example.memecommerceback.domain.emoji.repository.EmojiRepository;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto.EmojiDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmojiServiceImplV1 implements EmojiServiceV1 {

  private final ImageServiceV1 imageService;
  private final ProfanityFilterService profanityFilterService;

  private final EmojiRepository emojiRepository;

  @Override
  @Transactional
  public List<Emoji> register(
      List<MultipartFile> emojiImageList, Product product, User seller,
      String emojiPackName, List<EmojiDto> emojiDescriptionList) {
    // 1. 이모지 설명 욕설 존재하는지 확인
    profanityFilterService.validateListNoProfanity(
        emojiDescriptionList.stream().map(EmojiDto::getDescription).toList());

    // 2. 이모지 설명에 대한 이미지 업로드
    List<Image> imageList
        = imageService.uploadEmojiImage(
            emojiImageList, seller, product.getId());

    // 이전의 ProductService에서 해당 로직 존재하진 함.
    if (emojiImageList.size() != emojiDescriptionList.size()) {
      throw new EmojiCustomException(EmojiExceptionCode.COUNT_MISMATCH);
    }

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

  @Override
  @Transactional(readOnly = true)
  public List<Emoji> findAllByProductId(UUID productId) {
    return emojiRepository.findAllByProductId(productId);
  }

  @Override
  @Transactional
  public EmojiResponseDto updateOne(
      Long emojiId, String name, MultipartFile emojiImage, User seller) {
    // 1. 이모지의 주인인지?
    Emoji emoji = findById(emojiId);
    validateOwner(emoji.getUser().getId(), seller.getId());

    // name이 null이여도 false, 같지 않아도 false
    if (name != null && Objects.equals(emoji.getName(), name)
        && (emojiImage == null || emojiImage.isEmpty())) {
      throw new EmojiCustomException(EmojiExceptionCode.SAME_REQUEST_NAME);
    }

    if(name == null && (emojiImage == null || emojiImage.isEmpty())){
      throw new EmojiCustomException(EmojiExceptionCode.NO_CHANGE_DETECTED);
    }

    // 5. 변경 로직
    if (name != null && (emojiImage == null || emojiImage.isEmpty())) {
      // 이름만 변경
      profanityFilterService.validateNoProfanity(name);
      emoji.update(name);
    } else if ((name == null || Objects.equals(emoji.getName(), name))
        && emojiImage != null && !emojiImage.isEmpty()) {
      //
      Image newEmojiImage
          = imageService.changeEmojiImage(emojiImage, emoji, seller);
      emoji.updateImage(newEmojiImage);
    } else if (name != null && emojiImage != null && !emojiImage.isEmpty()) {
      profanityFilterService.validateNoProfanity(name);
      Image newEmojiImage
          = imageService.changeEmojiImage(emojiImage, emoji, seller);
      emoji.updateNameAndImage(name, newEmojiImage);
    }

    return EmojiConverter.toResponseDto(emoji);
  }

  @Override
  @Transactional
  public void deleteOne(Long emojiId, User seller) {
    Emoji emoji = findById(emojiId);
    validateOwner(emoji.getUser().getId(), seller.getId());

    imageService.deleteEmojiImage(emoji.getImage());
    emojiRepository.deleteById(emojiId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EmojiThumbnailResponseDto> readPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Emoji> emojiPage = emojiRepository.findAll(pageable);
    return EmojiConverter.toThumbnailResponseDtoPage(emojiPage);
  }

  @Override
  @Transactional(readOnly = true)
  public EmojiResponseDto readOne(Long emojiId) {
    Emoji emoji = findById(emojiId);
    return EmojiConverter.toResponseDto(emoji);
  }

  @Transactional(readOnly = true)
  public Emoji findById(Long emojiId){
    return emojiRepository.findById(emojiId).orElseThrow(
        ()-> new EmojiCustomException(EmojiExceptionCode.NOT_FOUND));
  }

  private void validateOwner(UUID emojiOwnerId, UUID sellerId){
    if (!emojiOwnerId.equals(sellerId)) {
      throw new EmojiCustomException(EmojiExceptionCode.NOT_OWNER);
    }
  }
}
