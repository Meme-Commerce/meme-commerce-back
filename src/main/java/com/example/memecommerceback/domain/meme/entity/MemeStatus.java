package com.example.memecommerceback.domain.meme.entity;

import lombok.Getter;

@Getter
public enum MemeStatus {
  PENDING,    // 검수 중
  APPROVED,   // 관리자 승인
  REJECTED,   // 관리자 거부
  BLOCKED     // 승인 후, 관리자의 판단에 의해 안보이게 처리
  ,;
}
