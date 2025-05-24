package com.example.memecommerceback.global.utils;

import java.util.HashSet;
import java.util.Set;

public class RabinKarpUtils {
  public static final int WINDOW_SIZE = 5;
  public static final double SIMILARITY_THRESHOLD = 90.0;
  private static final int PRIME = 101;

  // 모든 슬라이딩 윈도우 해시 집합 구하기
  public static Set<Integer> getAllWindowHashes(String s, int windowSize) {
    Set<Integer> hashes = new HashSet<>();
    if (s.length() < windowSize) {
      hashes.add(rabinKarpHash(s));
      return hashes;
    }
    for (int i = 0; i <= s.length() - windowSize; i++) {
      String window = s.substring(i, i + windowSize);
      hashes.add(rabinKarpHash(window));
    }
    return hashes;
  }

  // Rabin-Karp 해시 함수
  public static int rabinKarpHash(String s) {
    int hash = 0, pow = 1;
    for (char c : s.toCharArray()) {
      hash += c * pow;
      pow *= PRIME;
    }
    return hash;
  }

  // 윈도우 해시 겹침 비율(%) 계산
  public static double slidingWindowSimilarity(String a, String b, int windowSize) {
    Set<Integer> hashesA = getAllWindowHashes(a, windowSize);
    Set<Integer> hashesB = getAllWindowHashes(b, windowSize);
    int common = 0;
    for (int h : hashesA) {
      if (hashesB.contains(h)) common++;
    }
    if (hashesA.isEmpty()) return 0.0;
    return (double) common / hashesA.size() * 100.0;
  }
}
