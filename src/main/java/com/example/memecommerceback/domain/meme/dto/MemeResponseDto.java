package com.example.memecommerceback.domain.meme.dto;

import com.example.memecommerceback.domain.meme.entity.MemeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(name = "MemeResponseDto.CreateDto", description = "밈 여러 개 생성 응답 DTO")
  public static class CreateDto {
    @Schema(description = "등록자 닉네임", example = "memeUser01")
    private String registeredNickname;

    @Schema(description = "밈 상태", example = "PENDING")
    private MemeStatus status;

    @Schema(description = "생성된 밈 정보 리스트")
    private List<CreateOneDto> createOneDtoList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeResponseDto.CreateOneDto", description = "밈 하나 생성 정보 DTO")
  public static class CreateOneDto {
    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "밈 이름", example = "트랄랄레로 트랄랄라")
    private String name;

    @Schema(description = "밈 설명", example = "이탈리안 브레인 봇")
    private String description;

    @Schema(description = "밈 생성 시각", example = "2025-06-03T14:30:00")
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeResponseDto.UpdateOneStatusDto", description = "밈 하나 상태 수정 응답 DTO")
  public static class UpdateOneStatusDto {
    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "등록자 닉네임", example = "adminUser")
    private String registeredNickname;

    @Schema(description = "변경된 밈 상태", example = "APPROVED")
    private MemeStatus status;

    @Schema(description = "상태 수정 시각", example = "2025-06-03T15:01:00")
    private LocalDateTime modifiedAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeResponseDto.UpdateOneDto", description = "밈 하나 수정 응답 DTO")
  public static class UpdateOneDto {
    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "수정된 밈 이름", example = "트랄랄레로 트랄랄라2")
    private String name;

    @Schema(description = "수정된 밈 설명", example = "변경된 이탈리안 브레인 봇")
    private String description;

    @Schema(description = "등록자 닉네임", example = "memeUser01")
    private String registeredNickname;

    @Schema(description = "밈 상태", example = "PENDING")
    private MemeStatus status;

    @Schema(description = "밈 수정 시각", example = "2025-06-03T15:15:00")
    private LocalDateTime modifiedAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeResponseDto.ReadOneDto", description = "밈 단일 조회 응답 DTO")
  public static class ReadOneDto {
    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "밈 이름", example = "트랄랄레로 트랄랄라")
    private String name;

    @Schema(description = "밈 설명", example = "이탈리안 브레인 봇")
    private String description;

    @Schema(description = "등록자 닉네임", example = "memeUser01")
    private String registeredNickname;

    @Schema(description = "밈 상태", example = "APPROVED")
    private MemeStatus status;

    @Schema(description = "밈 생성 시각", example = "2025-06-03T14:30:00")
    private LocalDateTime createdAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(name = "MemeResponseDto.ReadSummaryOneDto", description = "밈 요약 조회 DTO")
  public static class ReadSummaryOneDto {
    @Schema(description = "밈 아이디", example = "101")
    private Long memeId;

    @Schema(description = "밈 이름", example = "트랄랄레로 트랄랄라")
    private String name;
  }
}
