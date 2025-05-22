package com.example.memecommerceback.global.utils;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.OAuth2CustomException;
import com.example.memecommerceback.global.oauth.constant.OAuthConstants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
  public static final String LOCAL_DATE_FORMATTER_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter localDateFormatter
      = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMATTER_PATTERN);

  public static LocalDate parse(String birthYear, String birthDay, String provider){
    switch (provider){
      case OAuthConstants.NAVER_PROVIDER -> {
        return LocalDate.parse(birthYear + "-" + birthDay, localDateFormatter);
      }
      case OAuthConstants.KAKAO_PROVIDER -> {
        String month = birthDay.substring(0, 2);
        String day = birthDay.substring(2, 4);
        String formattedDate = birthYear + "-" + month + "-" + day;
        return LocalDate.parse(formattedDate, localDateFormatter);
      }
      default -> throw new OAuth2CustomException(
          GlobalExceptionCode.NOT_FOUND_PROVIDER);
    }
  }

  public static Integer calculateAge(LocalDate birthDate){
    return LocalDate.now().getYear() - birthDate.getYear() + 1;
  }
}
