package com.example.memecommerceback.domain.hashtags.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HashtagResponseDto {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "HashtagResponseDto.CreateDto",
      description = "해시태그 여러 개 생성 응답 DTO")
  public static class CreateDto {
    @Schema(description = "생성된 해시태그 정보 리스트")
    private List<CreateOneDto> createOneDtoList;

    @Schema(description = "해시태그 생성 시각", example = "2025-05-28T21:20:40")
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "HashtagResponseDto.CreateOneDto",
      description = "해시태그 하나 생성 정보 DTO")
  public static class CreateOneDto {
    @Schema(description = "해시태그 아이디", example = "1")
    private Long hashtagId;

    @Schema(description = "해시태그 명", example = "트렌드")
    private String name;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "HashtagResponseDto.UpdateOneDto",
      description = "해시태그 하나 수정 응답 DTO")
  public static class UpdateOneDto {
    @Schema(description = "해시태그 아이디", example = "1")
    private Long hashtagId;

    @Schema(description = "수정된 해시태그 명", example = "밈")
    private String name;

    @Schema(description = "해시태그 수정 시각", example = "2025-05-29T09:10:00")
    private LocalDateTime modifiedAt;
  }
}
