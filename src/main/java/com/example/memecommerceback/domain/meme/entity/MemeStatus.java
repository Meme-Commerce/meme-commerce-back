package com.example.memecommerceback.domain.meme.entity;

import lombok.Getter;

@Getter
public enum MemeStatus {
  PENDING,    // 검수 중
  APPROVED,   // 관리자 승인
  REJECTED,   // 관리자 거부
  ;
}
