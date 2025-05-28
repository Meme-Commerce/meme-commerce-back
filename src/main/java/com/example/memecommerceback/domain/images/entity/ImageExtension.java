package com.example.memecommerceback.domain.images.entity;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ImageExtension {
  PNG(Name.PNG),
  JPG(Name.JPG),
  JPEG(Name.JPEG);
  private final String ext;

  ImageExtension(String ext){
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

  public static ImageExtension from(String ext) {
    return Arrays.stream(values())
        .filter(e -> e.ext.equalsIgnoreCase(ext))
        .findFirst()
        .orElseThrow(() -> new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION));
  }
}
