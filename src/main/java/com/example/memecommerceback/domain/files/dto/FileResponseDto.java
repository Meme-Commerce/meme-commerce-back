package com.example.memecommerceback.domain.files.dto;

import com.example.memecommerceback.domain.files.entity.FileExtension;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {
  @Schema(description = "파일 ID", example = "8cfdc5a4-18d8-42c4-b40b-4d3f30351c11")
  private UUID fileId;

  @Schema(description = "파일 URL",
      example = "https://meme-commerce-bucket.s3.ap-northeast-2.amazonaws.com/users/dbnickname/files/randomuuid_filename.png")
  private String url;

  @Schema(description = "확장자", example = "PDF")
  private FileExtension extension;
}

