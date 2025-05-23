package com.example.memecommerceback.domain.images.entity;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import java.util.Arrays;

public enum Extension {
  PNG(Name.PNG),
  JPG(Name.JPG),
  JPEG(Name.JPEG);
  private final String ext;

  Extension(String ext){
    this.ext = ext;
  }

  public static class Name{
    public static final String PNG = "png";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
  }

  public static boolean isSupported(String ext) {
    if (ext == null) return false;
    return Arrays.stream(values())
        .anyMatch(e -> e.ext.equalsIgnoreCase(ext));
  }

  public static Extension from(String ext) {
    return Arrays.stream(values())
        .filter(e -> e.ext.equalsIgnoreCase(ext))
        .findFirst()
        .orElseThrow(() -> new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION));
  }

  public static Extension extractFromFilename(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      throw new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION);
    }
    String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    return from(ext); // 이미 있는 Enum 변환 및 검증 로직 재사용
  }
}
