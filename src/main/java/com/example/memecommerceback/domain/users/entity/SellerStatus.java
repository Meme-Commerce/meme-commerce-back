package com.example.memecommerceback.domain.users.entity;

import lombok.Getter;

@Getter
public enum SellerStatus {
  NONE,           // 일반 유저 또는 관리자 (SELLER 신청 전)
  PENDING,        // SELLER 신청, 검수 대기중
  APPROVED,       // SELLER 권한 부여됨
  REJECTED,
  ;      // SELLER 거절됨

}
