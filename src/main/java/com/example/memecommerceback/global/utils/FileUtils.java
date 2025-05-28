package com.example.memecommerceback.global.utils;

import com.example.memecommerceback.domain.files.entity.FileExtension;
import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.entity.ImageExtension;

public class FileUtils {
  public static ImageExtension extractFromImageName(String imageName) {
    if (imageName == null || !imageName.contains(".")) {
      throw new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION);
    }
    String ext = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
    return ImageExtension.from(ext); // 이미 있는 Enum 변환 및 검증 로직 재사용
  }

  public static FileExtension extractFromFilename(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      throw new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION);
    }
    String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    return FileExtension.from(ext); // 이미 있는 Enum 변환 및 검증 로직 재사용
  }
}
