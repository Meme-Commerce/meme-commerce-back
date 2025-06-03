package com.example.memecommerceback.domain.memeEmoji.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemeEmojiExceptionCode {

  NOT_FOUND(
      HttpStatus.NOT_FOUND, "MEME-EMOJI-001",
      "해당 밈모지를 찾을 수 없습니다."),
  NOT_OWNER(
      HttpStatus.BAD_REQUEST, "MEME-EMOJI-002",
      "해당 밈모지를 만든 사람이 아닙니다."),
  ALREADY_COMPLETED_STATUS(
      HttpStatus.BAD_REQUEST, "MEME-EMOJI-003",
      "이미 완료된 상태(APPROVED, REJECTED)라 변경할 수 없습니다."
  ),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}