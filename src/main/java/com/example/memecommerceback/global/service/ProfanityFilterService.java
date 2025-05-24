package com.example.memecommerceback.global.service;

import java.util.List;

public interface ProfanityFilterService {

  boolean contains(String text);

  void validateNoProfanity(String text);

  void validateListNoProfanity(List<String> textList);
}
