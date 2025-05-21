package com.example.memecommerceback.domain.user.entity;

import java.util.Arrays;
import java.util.List;

public enum Gender {
  MALE("M", List.of("male", "m")),
  FEMALE("F", List.of("female", "f")),
  UNKNOWN("U", List.of("unknown", "u"));

  private final String code;
  private final List<String> aliases;

  Gender(String code, List<String> aliases) {
    this.code = code;
    this.aliases = aliases;
  }

  public String getCode() {
    return code;
  }

  public List<String> getAliases() {
    return aliases;
  }

  // "F", "female", "M", "male" 등 다양한 입력에 대응
  public static Gender fromCode(String code) {
    if (code == null) return UNKNOWN;
    String input = code.trim().toLowerCase();

    return Arrays.stream(values())
        .filter(g -> g.aliases.stream()
            .anyMatch(alias -> alias.equalsIgnoreCase(input)))
        .findFirst()
        .orElse(UNKNOWN);
  }

  public static String toCode(Gender gender) {
    if (gender == null || gender.getCode() == null) {
      return "UNKNOWN";
    }
    return gender.getCode();
  }
}
