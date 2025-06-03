package com.example.memecommerceback.domain.memeEmoji.service;

import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 밈-이모지(Meme-Emoji, 밈모지) 연결 관리를 위한 서비스 인터페이스입니다.
 * <p>
 * 밈과 이모지의 연결 생성, 상태 변경(승인/반려), 수정, 조회, 삭제 등
 * 밈-이모지 조합에 대한 주요 비즈니스 로직을 정의합니다.
 * 관리자의 밈모지 승인/반려, 사용자 생성/수정/조회, 일괄 삭제 등
 * 다양한 상황에 활용됩니다.
 * </p>
 *
 * @author MemeCommerce Team
 * @version 1.0
 * @since 1.0
 */
public interface MemeEmojiServiceV1 {

  /**
   * 밈모지(밈-이모지 연결)를 하나 생성합니다.
   * <p>
   * 특정 밈과 이모지의 조합에 대해 요청자의 메시지와 함께 연결 엔티티를 생성합니다.
   * 이름, 요청 메시지 등은 유효성 검증 후 등록됩니다.
   * </p>
   *
   * @param memeId     연결할 밈의 고유 식별자
   * @param emojiId    연결할 이모지의 고유 식별자
   * @param name       밈모지 이름
   * @param message    사용자의 요청 메시지(요청 사항)
   * @param loginUser  연결을 생성하는 사용자 엔티티
   * @return 생성된 밈모지 정보 응답 DTO
   */
  MemeEmojiResponseDto.CreateOneDto createOne(
      Long memeId, Long emojiId, String name, String message, User loginUser);

  /**
   * 관리자가 밈모지 상태(승인/반려)를 수정합니다.
   * <p>
   * 밈모지의 승인 여부와 결정 사유를 받아, 상태를 변경(승인/반려)합니다.
   * 승인된 밈모지는 공개될 수 있으며, 반려 시 사유가 전달됩니다.
   * </p>
   *
   * @param memeEmojiId 상태를 변경할 밈모지의 고유 식별자
   * @param isApproved  승인 여부(true: 승인, false: 반려)
   * @param reason      승인/반려 사유 메시지
   * @param loginUser   관리자 유저 엔티티
   * @return 변경된 상태 정보 및 응답 DTO
   */
  MemeEmojiResponseDto.UpdateOneDto updateOneStatusByAdmin(
      Long memeEmojiId, boolean isApproved, String reason, User loginUser);

  /**
   * 사용자가 밈모지의 내용을 수정합니다.
   * <p>
   * 밈모지의 이름, 요청 메시지 등 내용을 변경합니다.
   * 유효성 검증 및 권한 확인이 내부에서 동작할 수 있습니다.
   * </p>
   *
   * @param memeEmojiId 수정할 밈모지의 고유 식별자
   * @param name        수정할 밈모지 이름
   * @param message     수정할 요청 메시지(요청 사항)
   * @param loginUser   요청 사용자(등록자) 엔티티
   * @return 수정된 밈모지 정보 응답 DTO
   */
  MemeEmojiResponseDto.UpdateOneDto updateOne(
      Long memeEmojiId, String name, String message, User loginUser);

  /**
   * 밈모지 목록을 페이지 단위로 조회합니다.
   * <p>
   * 전체 밈모지 리스트를 페이지네이션으로 조회합니다.
   * 권한에 따라 접근 가능한 범위가 다를 수 있습니다.
   * </p>
   *
   * @param page 페이지 번호(0부터 시작)
   * @param size 한 페이지당 데이터 개수
   * @return 밈모지 정보의 페이지 응답 DTO
   */
  Page<MemeEmojiResponseDto.ReadOneDto> readPage(int page, int size);

  /**
   * 밈모지를 단일 조회합니다.
   * <p>
   * 특정 밈모지의 상세 정보를 반환합니다.
   * 권한 및 상태(공개/비공개 등)에 따라 예외가 발생할 수 있습니다.
   * </p>
   *
   * @param memeEmojiId 조회할 밈모지의 고유 식별자
   * @param loginUser   요청 사용자 엔티티
   * @return 밈모지 상세 정보 응답 DTO
   */
  MemeEmojiResponseDto.ReadOneDto readOne(Long memeEmojiId, User loginUser);

  /**
   * 관리자 권한으로 여러 밈모지를 일괄 삭제합니다.
   * <p>
   * 밈모지 ID 리스트와 삭제 사유를 받아, 해당 밈모지들을 일괄 삭제합니다.
   * 존재하지 않는 밈모지, 권한 문제 등은 내부에서 예외 처리됩니다.
   * </p>
   *
   * @param deletedMemeIdList 삭제할 밈모지 ID 리스트
   * @param deletedReason     삭제 사유(로그 등)
   * @param admin             관리자 유저 엔티티
   */
  void deleteMany(
      List<Long> deletedMemeIdList, String deletedReason, User admin);

  /**
   * 밈모지 ID로 밈모지 엔티티를 직접 조회합니다.
   * <p>
   * 내부 로직(상세, 수정, 삭제 등)에서 활용되는 메서드입니다.
   * </p>
   *
   * @param memeEmojiId 조회할 밈모지의 고유 식별자
   * @return 밈모지 엔티티 객체
   */
  MemeEmoji findById(Long memeEmojiId);
}
