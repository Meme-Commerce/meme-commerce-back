package com.example.memecommerceback.global.service;

import com.example.memecommerceback.global.exception.CustomException;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.ProfanityFilterCustomException;
import com.vane.badwordfiltering.BadWordFiltering;
import org.springframework.stereotype.Service;

@Service
public class ProfanityFilterServiceImpl implements ProfanityFilterService {

  private final BadWordFiltering badWordFiltering = new BadWordFiltering();

  public boolean contains(String text) {
    return badWordFiltering.check(text);
  }

  public void validateNoProfanity(String text) {
    if (blankCheck(text)) {
      throw new ProfanityFilterCustomException(GlobalExceptionCode.NOT_BLANK);
    }
    if (badWordFiltering.check(text)) {
      throw new ProfanityFilterCustomException(GlobalExceptionCode.PROFANITY_DETECTED);
    }
  }

  private boolean blankCheck(String text) {
    return text == null || text.trim().isEmpty();
  }
}
