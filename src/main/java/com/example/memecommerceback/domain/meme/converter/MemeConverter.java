package com.example.memecommerceback.domain.meme.converter;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto.CreateDto;
import com.example.memecommerceback.domain.meme.entity.Meme;
import java.util.List;

public class MemeConverter {
  public static List<Meme> toEntityList(MemeRequestDto.CreateDto requestDto){
    return requestDto.getCreateOneDtoList().stream()
        .map(MemeConverter::toEntity)
        .toList();
  }
  public static Meme toEntity(MemeRequestDto.CreateOneDto requestDto){
    return Meme.builder()
        .name(requestDto.getName())
        .description(requestDto.getDescription())
        .build();
  }

  public static MemeResponseDto.CreateDto toCreateDto(List<Meme> memeList){
    List<MemeResponseDto.CreateOneDto> createOneDtoList
        = memeList.stream().map(MemeConverter::toCreateOneDto).toList();
    return MemeResponseDto.CreateDto.builder()
        .createOneDtoList(createOneDtoList)
        .build();
  }

  public static MemeResponseDto.CreateOneDto toCreateOneDto(Meme meme){
    return MemeResponseDto.CreateOneDto.builder()
        .memeId(meme.getId())
        .name(meme.getName())
        .description(meme.getDescription())
        .createdAt(meme.getCreatedAt())
        .build();
  }
}
