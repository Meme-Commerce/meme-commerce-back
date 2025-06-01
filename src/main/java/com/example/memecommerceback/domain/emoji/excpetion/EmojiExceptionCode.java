package com.example.memecommerceback.domain.emoji.excpetion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmojiExceptionCode {

  NOT_FOUND(
      HttpStatus.BAD_REQUEST, "EMOJI-001",
      "찾을 수 없는 이모지입니다."),
  NEED_TO_REGISTER_NICKNAME(
      HttpStatus.BAD_REQUEST, "EMOJI-002",
      "닉네임을 등록한 이후, 이모지를 등록할 수 있습니다."),
  NOT_OWNER(
      HttpStatus.BAD_REQUEST, "EMOJI-003",
      "해당 이모지의 주인이 아닙니다."),
  SAME_REQUEST_NAME(
      HttpStatus.BAD_REQUEST, "EMOJI-004",
      "동일한 이모지 이름으로 요청하였습니다."),
  REGISTER_IMAGE(
      HttpStatus.BAD_REQUEST, "EMOJI-005",
      "이모지 이미지를 수정할 때, 이미지를 등록하셔야합니다."),
  NO_CHANGE_DETECTED(
      HttpStatus.BAD_REQUEST, "EMOJI-006",
      "이모지 변경 사항이 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}