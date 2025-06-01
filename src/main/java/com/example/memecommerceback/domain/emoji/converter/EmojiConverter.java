package com.example.memecommerceback.domain.emoji.converter;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto.EmojiDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmojiConverter {

  public static Emoji toEntity(
      String name, User seller, Image image, Product product) {
    return Emoji.builder()
        .name(name)
        .user(seller)
        .image(image)
        .product(product)
        .build();
  }

  public static List<Emoji> toEntityList(
      List<Image> imageList, List<EmojiDto> descriptionList, Product product, User seller) {
    return IntStream.range(0, imageList.size())
        .mapToObj(i -> Emoji.builder()
            .name(descriptionList.get(i).getDescription())
            .user(seller)
            .image(imageList.get(i))
            .product(product)
            .build())
        .collect(Collectors.toList());
  }

  public static List<EmojiResponseDto> toResponseDtoList(List<Emoji> emojiList){
    return emojiList.stream().map(EmojiConverter::toResponseDto).toList();
  }

  public static EmojiResponseDto toResponseDto(Emoji emoji){
    return EmojiResponseDto.builder()
        .emojiId(emoji.getId())
        .name(emoji.getName())
        .modifiedAt(emoji.getModifiedAt())
        .createdAt(emoji.getCreatedAt())
        .emojiImageResponseDto(
            ImageConverter.toResponseDto(emoji.getImage()))
        .build();
  }
}
