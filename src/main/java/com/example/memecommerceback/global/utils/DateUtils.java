package com.example.memecommerceback.global.utils;

import com.example.memecommerceback.global.exception.DateCustomException;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.OAuth2CustomException;
import com.example.memecommerceback.global.oauth.constant.OAuthConstants;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

  public static void validateDateTime(LocalDateTime startDate, LocalDateTime endDate){
    LocalDateTime now = LocalDateTime.now();

    // 1. 시작일, 마감일이 오늘보다 빠른 경우
    if(startDate.isBefore(now) || endDate.isBefore(now)){
      throw new DateCustomException(GlobalExceptionCode.DATE_BEFORE_TODAY);
    }

    // 2. 시작일이 마감일보다 느린 경우
    if(startDate.isAfter(endDate)){
      throw new DateCustomException(GlobalExceptionCode.END_DATE_BEFORE_START_DATE);
    }

    // 3. 요청 기간이 7일 이상이 아닌 경우
    if(ChronoUnit.DAYS.between(startDate, endDate) < 7L){
      throw new DateCustomException(GlobalExceptionCode.REQUEST_SEVEN_DAYS);
    }

    // 4. 현재의 날로부터 3달 이내의 시작일만 가능
    if(startDate.isAfter(now.plusMonths(3))) {
      throw new DateCustomException(GlobalExceptionCode.DATE_TOO_FAR);
    }
  }
}