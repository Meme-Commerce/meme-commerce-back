package com.example.memecommerceback.domain.users.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "USER-001",
      "해당 유저를 찾을 수 없습니다."),
  NOT_EXIST_AUTHORITY(
      HttpStatus.BAD_REQUEST, "USER-002",
      "해당 권한이 존재하지 않습니다."),
  NEED_TO_REGISTER_NICKNAME(
      HttpStatus.BAD_REQUEST, "USER-003",
      "먼저 닉네임을 설정하셔야 이용할 수 있습니다."
          + "닉네임만 먼저 설정해주세요"),
  REQUEST_DUPLICATE_NICKNAME(
      HttpStatus.BAD_REQUEST, "USER-004",
      "요청하신 닉네임은 중복 닉네임입니다. "),
  ONLY_SELF_OR_ADMIN_CAN_DELETE(
      HttpStatus.BAD_REQUEST, "USER-005",
      "오직 자신 또는 관리자만 회원을 탈퇴할 권한이 있습니다."),
  ALREADY_COMPLETED_OR_PENDING_STATUS(
      HttpStatus.BAD_REQUEST, "USER-006",
      "이미 완료된 상태(거절, 판매자 승인)이거나 검수 중인 상태입니다."),
  ALREADY_COMPLETED_STATUS(
      HttpStatus.BAD_REQUEST, "USER-007",
      "이미 검수가 완료된 상태입니다."),
  CANNOT_CHANGE_ADMIN_ROLE(
      HttpStatus.BAD_REQUEST, "USER-008",
      "관리자는 다른 관리자의 권한을 변경할 수 없습니다."),
  CONFLICT_IMAGE_UPDATE_AND_DELETE(
      HttpStatus.BAD_REQUEST, "USER-009",
      "프로필 이미지 삭제와 업로드를 동시에 요청할 수 없습니다."),
  EXIST_NICKNAME(
      HttpStatus.BAD_REQUEST, "USER-010",
      "중복되는 닉네임이 존재합니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}
