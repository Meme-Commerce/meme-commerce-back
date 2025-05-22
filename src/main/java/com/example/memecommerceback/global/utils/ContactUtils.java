package com.example.memecommerceback.global.utils;

public class ContactUtils {

  public static String normalizeContact(String contact) {
    String digitsOnly;

    if (contact.startsWith("+82")) {
      // +82 10-1234-5678 → 01012345678
      digitsOnly = "0" + contact.substring(contact.indexOf(" ") + 1).replaceAll("[^0-9]", "");
    } else if (contact.startsWith("010")) {
      // 010-1234-5678 or 01012345678 → 01012345678
      digitsOnly = contact.replaceAll("[^0-9]", "");
    } else {
      throw new IllegalArgumentException("유효한 전화번호 입력이 아닙니다.");
    }

    // 유효성 체크: 11자리 (010xxxxxxxx)
    if (!digitsOnly.matches("^010\\d{8}$")) {
      throw new IllegalArgumentException("유효한 전화번호 입력이 아닙니다.");
    }

    // 010-1234-5678 포맷으로 변환
    return digitsOnly.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
  }
}

