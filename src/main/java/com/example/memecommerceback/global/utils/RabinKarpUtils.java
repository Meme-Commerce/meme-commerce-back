package com.example.memecommerceback.global.utils;

import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.RabinKarpCustomException;
import java.util.HashSet;
import java.util.Set;

public class RabinKarpUtils {
  public static final int WINDOW_SIZE = 5;
  public static final double SIMILARITY_THRESHOLD = 90.0;
  public static final long MOD = 1000000007L;
  private static final int PRIME = 101;

  // 모든 슬라이딩 윈도우 해시 집합 구하기
  public static Set<Integer> getAllWindowHashes(String s, int windowSize) {
    if(windowSize <= 0){
      throw new RabinKarpCustomException(
          GlobalExceptionCode.WINDOW_SIZE_MUST_BE_POSITIVE);
    }
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
    long hash = 0, pow = 1;
    for (char c : s.toCharArray()) {
      hash = (hash + (c * pow) % MOD) % MOD;
      pow = (pow * PRIME) % MOD;
    }
    return (int)hash;
  }

  // 윈도우 해시 겹침 비율(%) 계산
  public static double slidingWindowSimilarity(String a, String b, int windowSize) {
    if (a == null || b == null)
      throw new IllegalArgumentException("입력 문자열은 null일 수 없습니다");
    Set<Integer> hashesA = getAllWindowHashes(a, windowSize);
    Set<Integer> hashesB = getAllWindowHashes(b, windowSize);

    Set<Integer> intersection = new HashSet<>(hashesA);
    intersection.retainAll(hashesB);

    Set<Integer> union = new HashSet<>(hashesA);
    union.addAll(hashesB);

    if (union.isEmpty()) return 0.0;
    return (double) intersection.size() / union.size() * 100.0;
  }

}
