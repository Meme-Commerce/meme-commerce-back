package com.example.memecommerceback.domain.meme.converter;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.meme.entity.MemeStatus;
import java.util.List;
import org.springframework.data.domain.Page;

public class MemeConverter {
  public static List<Meme> toEntityList(
      MemeRequestDto.CreateDto requestDto, String nickname,
      int currentYear, int currentQuarter){
    return requestDto.getCreateOneDtoList().stream()
        .map(requestOneDto -> toEntity(requestOneDto, nickname, currentYear, currentQuarter))
        .toList();
  }
  public static Meme toEntity(
      MemeRequestDto.CreateOneDto requestDto, String nickname, int year, int quarter){
    return Meme.builder()
        .name(requestDto.getName())
        .description(requestDto.getDescription())
        .registeredNickname(nickname)
        .year(year)
        .quarter(quarter)
        .build();
  }

  public static MemeResponseDto.CreateDto toCreateDto(
      List<Meme> memeList, String registeredNickname){
    List<MemeResponseDto.CreateOneDto> createOneDtoList
        = memeList.stream().map(MemeConverter::toCreateOneDto).toList();
    return MemeResponseDto.CreateDto.builder()
        .createOneDtoList(createOneDtoList)
        .registeredNickname(registeredNickname)
        .status(MemeStatus.PENDING)
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

  public static MemeResponseDto.UpdateOneStatusDto toUpdateOneStatusDto(Meme meme){
    return MemeResponseDto.UpdateOneStatusDto.builder()
        .memeId(meme.getId())
        .status(meme.getStatus())
        .registeredNickname(meme.getRegisteredNickname())
        .modifiedAt(meme.getModifiedAt())
        .build();
  }

  public static MemeResponseDto.UpdateOneDto toUpdateOneDto(Meme meme){
    return MemeResponseDto.UpdateOneDto.builder()
        .memeId(meme.getId())
        .name(meme.getName())
        .description(meme.getDescription())
        .status(meme.getStatus())
        .registeredNickname(meme.getRegisteredNickname())
        .modifiedAt(meme.getModifiedAt())
        .build();
  }

  public static Page<MemeResponseDto.ReadOneDto> toReadPage(Page<Meme> memePage){
    return memePage.map(MemeConverter::toReadOneDto);
  }

  public static MemeResponseDto.ReadOneDto toReadOneDto(Meme meme){
    return MemeResponseDto.ReadOneDto.builder()
        .memeId(meme.getId())
        .name(meme.getName())
        .description(meme.getDescription())
        .status(meme.getStatus())
        .registeredNickname(meme.getRegisteredNickname())
        .createdAt(meme.getCreatedAt())
        .build();
  }

  public static Page<MemeResponseDto.ReadSummaryOneDto> toSummaryReadPage(
      Page<Meme> memePage){
    return memePage.map(MemeConverter::toReadSummaryOneDto);
  }

  public static MemeResponseDto.ReadSummaryOneDto toReadSummaryOneDto(
      Meme meme){
    return MemeResponseDto.ReadSummaryOneDto.builder()
        .memeId(meme.getId()).name(meme.getName()).build();
  }
}
