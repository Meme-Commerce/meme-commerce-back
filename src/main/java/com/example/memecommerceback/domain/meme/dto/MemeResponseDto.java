package com.example.memecommerceback.domain.meme.dto;

import com.example.memecommerceback.domain.meme.entity.MemeStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemeResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateDto {
    private String registeredNickname;
    private MemeStatus status;
    private List<CreateOneDto> createOneDtoList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateOneDto {
    private Long memeId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateOneStatusDto {
    private Long memeId;
    private String registeredNickname;
    private MemeStatus status;
    private LocalDateTime modifiedAt;
  }
}
