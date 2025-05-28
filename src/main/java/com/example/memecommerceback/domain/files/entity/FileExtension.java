package com.example.memecommerceback.domain.files.entity;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.images.entity.ImageExtension;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum FileExtension {
  PDF(Name.PDF),
  HWP(Name.HWP),
  DOCX(Name.DOCX),
  XLSX(Name.XLSX),
  TXT(Name.TXT),
  ZIP(Name.ZIP);

  private final String ext;

  FileExtension(String ext){
    this.ext = ext;
  }

  public static class Name{
    public static final String PDF = "pdf";
    public static final String HWP = "hwp";
    public static final String DOCX = "docx";
    public static final String XLSX = "xlsx";
    public static final String TXT = "txt";
    public static final String ZIP = "zip";
  }

  public static boolean isSupported(String ext) {
    if (ext == null) return false;
    return Arrays.stream(values())
        .anyMatch(e -> e.ext.equalsIgnoreCase(ext));
  }

  public static FileExtension from(String ext) {
    return Arrays.stream(values())
        .filter(e -> e.ext.equalsIgnoreCase(ext))
        .findFirst()
        .orElseThrow(() -> new FileCustomException(FileExceptionCode.NOT_SUPPORTED_EXTENSION));
  }
}
