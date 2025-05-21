package com.example.memecommerceback.global.service;

public interface ProfanityFilterService {

  boolean contains(String text);

  void validateNoProfanity(String text);
}
