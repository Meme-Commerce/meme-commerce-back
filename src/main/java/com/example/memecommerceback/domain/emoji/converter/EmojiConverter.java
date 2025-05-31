package com.example.memecommerceback.domain.emoji.converter;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.users.entity.User;

public class EmojiConverter {
  public static Emoji toEntityDefaultType(
      String name, User admin, Image image){
    return Emoji.builder()
        .name(name)
        .user(admin)
        .image(image)
        .build();
  }
}
