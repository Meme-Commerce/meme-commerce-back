package com.example.memecommerceback.domain.files.converter;

import com.example.memecommerceback.domain.files.dto.FileResponseDto;
import com.example.memecommerceback.domain.files.entity.File;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3FileResponseDto;
import java.util.List;

public class FileConverter {

  public static File toEntity(S3FileResponseDto s3ResponseDto, User owner) {
    return File.builder()
        .url(s3ResponseDto.getUrl())
        .size(s3ResponseDto.getSize())
        .extension(s3ResponseDto.getExtension())
        .fileName(s3ResponseDto.getFileName())
        .originalName(s3ResponseDto.getOriginalName())
        .owner(owner)
        .build();
  }

  public static List<File> toEntityList(
      List<S3FileResponseDto> s3ResponseDtoList, User owner) {
    return s3ResponseDtoList.stream().map(
        s3ResponseDto -> {
          return toEntity(s3ResponseDto, owner);
        }
    ).toList();
  }

  public static List<FileResponseDto> toResponseDtoList(List<File> fileList) {
    return fileList.stream().map(FileConverter::toResponseDto).toList();
  }

  public static FileResponseDto toResponseDto(File file) {
    return FileResponseDto.builder()
        .fileId(file.getId())
        .extension(file.getExtension())
        .url(file.getUrl())
        .build();
  }
}
