package com.example.memecommerceback.domain.emoji.converter;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.images.converter.ImageConverter;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;

public class EmojiConverter {
  public static Emoji toEntity(String name, User admin, Image image){
    return Emoji.builder().name(name).user(admin).image(image).build();
  }

  public static EmojiResponseDto toResponseDto(Emoji emoji){
    return EmojiResponseDto.builder()
        .emojiId(emoji.getId())
        .name(emoji.getName())
        .createdAt(emoji.getCreatedAt())
        .modifiedAt(emoji.getModifiedAt())
        .imageResponseDto(ImageConverter.toResponseDto(emoji.getImage()))
        .build();
  }
}
