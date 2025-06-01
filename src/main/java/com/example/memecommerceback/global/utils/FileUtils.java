package com.example.memecommerceback.global.utils;

import com.example.memecommerceback.domain.files.entity.FileExtension;
import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.entity.ImageExtension;

public class FileUtils {
  public static ImageExtension extractExtensionFromImageName(String imageName) {
    String ext = extractExtension(imageName);
    return ImageExtension.from(ext);
  }

  public static FileExtension extractExtensionFromFilename(String fileName) {
    String ext = extractExtension(fileName);
    return FileExtension.from(ext);
  }

  private static String extractExtension(String fileName){
    if (fileName == null || !fileName.contains(".")) {
      throw new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION);
    }
    return fileName.substring(fileName.lastIndexOf('.') + 1)
        .toLowerCase();
  }
}
