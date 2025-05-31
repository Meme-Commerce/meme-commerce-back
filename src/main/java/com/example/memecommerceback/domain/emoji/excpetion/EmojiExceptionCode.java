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
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}