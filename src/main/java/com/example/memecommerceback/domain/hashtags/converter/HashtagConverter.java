package com.example.memecommerceback.domain.hashtags.converter;

import com.example.memecommerceback.domain.hashtags.dto.HashtagRequestDto;
import com.example.memecommerceback.domain.hashtags.dto.HashtagResponseDto;
import com.example.memecommerceback.domain.hashtags.dto.HashtagResponseDto.CreateDto;
import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import java.time.LocalDateTime;
import java.util.List;

public class HashtagConverter {

  public static Hashtag toEntity(String name){
    return Hashtag.builder().name(name).build();
  }

  public static List<Hashtag> toEntityList(
      HashtagRequestDto.CreateDto requestDto){
    return requestDto.getNameList().stream()
        .map(HashtagConverter::toEntity)
        .toList();
  }

  public static HashtagResponseDto.CreateDto toCreateDto(
      List<Hashtag> hashtagList){
    LocalDateTime createdAt = hashtagList.stream()
        .map(Hashtag::getCreatedAt)
        .max(LocalDateTime::compareTo)
        .orElse(null);

    List<HashtagResponseDto.CreateOneDto> createOneDtoList
        = hashtagList.stream().map(HashtagConverter::toCreateOneDto).toList();

    return CreateDto.builder().createOneDtoList(createOneDtoList)
        .createdAt(createdAt)
        .build();
  }

  public static HashtagResponseDto.CreateOneDto toCreateOneDto(
      Hashtag hashtag){
    return HashtagResponseDto.CreateOneDto.builder()
        .hashtagId(hashtag.getId())
        .name(hashtag.getName())
        .build();
  }

  public static HashtagResponseDto.UpdateOneDto toUpdateOneDto(Hashtag hashtag){
    return HashtagResponseDto.UpdateOneDto.builder()
        .hashtagId(hashtag.getId())
        .name(hashtag.getName())
        .modifiedAt(hashtag.getModifiedAt())
        .build();
  }
}
