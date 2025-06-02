package com.example.memecommerceback.domain.meme.service;

import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 밈(Meme) 관리를 위한 서비스 인터페이스입니다.
 * <p>
 * 밈의 생성, 수정, 삭제, 승인/반려, 상세·페이지 조회 등
 * 핵심 비즈니스 로직을 정의합니다. 관리자의 밈 승인/반려, 통계/요약 조회, 일반 사용자 수정 등
 * 다양한 기능에 활용됩니다.
 * </p>
 *
 * @author MemeCommerce Team
 * @version 1.0
 * @since 1.0
 */
public interface MemeServiceV1 {

  /**
   * 밈(여러 개)을 생성합니다.
   * <p>
   * 밈 이름/설명 리스트와 등록자 정보를 받아 여러 개의 밈을 한 번에 등록합니다.
   * 욕설/중복/유사도 검증 등 유효성 검사도 포함될 수 있습니다.
   * </p>
   *
   * @param requestDto 생성할 밈 데이터 리스트를 포함한 요청 DTO
   * @param user       등록자(유저) 엔티티
   * @return 생성된 밈 정보와 생성 결과를 담은 응답 DTO
   */
  MemeResponseDto.CreateDto create(MemeRequestDto.CreateDto requestDto, User user);

  /**
   * 관리자가 밈 상태(승인/반려)를 단건 수정합니다.
   * <p>
   * 밈의 승인 여부와 알림 메시지를 받아, 상태를 변경(승인/반려)합니다.
   * 승인된 밈만 공개될 수 있으며, 반려 시 알림 메시지가 전달됩니다.
   * </p>
   *
   * @param memeId               상태를 변경할 밈의 고유 식별자
   * @param isApproved           승인 여부(true: 승인, false: 반려)
   * @param notificationMessage  승인/반려 시 사용자에게 전달할 메시지
   * @param admin                관리자 유저 엔티티
   * @return 변경된 상태 정보 및 응답 DTO
   */
  MemeResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      Long memeId, boolean isApproved, String notificationMessage, User admin);

  /**
   * 사용자가 자신의 밈을 수정합니다.
   * <p>
   * 밈의 이름/설명 등 내용을 수정합니다. 욕설/중복/유사도 검증 등 유효성 검사가 내부에서 동작할 수 있습니다.
   * </p>
   *
   * @param memeId     수정할 밈의 고유 식별자
   * @param requestDto 수정할 밈의 내용이 담긴 요청 DTO
   * @param user       요청 사용자(등록자) 엔티티
   * @return 수정된 밈 정보와 응답 DTO
   */
  MemeResponseDto.UpdateOneDto updateOne(Long memeId, MemeRequestDto.UpdateOneDto requestDto, User user);

  /**
   * 관리자가 밈을 연도/분기별로 페이지네이션 조회합니다.
   * <p>
   * 관리자 권한으로 연도 및 분기 조건을 기반으로 전체 밈 목록을 페이지 단위로 조회합니다.
   * </p>
   *
   * @param page    페이지 번호(0부터 시작)
   * @param size    한 페이지당 데이터 개수
   * @param year    조회할 연도(YYYY)
   * @param quarter 조회할 분기(1~4)
   * @return 밈 상세 정보의 페이지 응답 DTO
   */
  Page<MemeResponseDto.ReadOneDto> readPageByAdmin(int page, int size, int year, int quarter);

  /**
   * 일반 사용자가 연도/분기별로 밈 요약 정보를 페이지네이션 조회합니다.
   * <p>
   * 연도 및 분기별로 공개된(승인된) 밈의 요약 정보를 페이지 단위로 제공합니다.
   * </p>
   *
   * @param page    페이지 번호(0부터 시작)
   * @param size    한 페이지당 데이터 개수
   * @param year    조회할 연도(YYYY)
   * @param quarter 조회할 분기(1~4)
   * @return 밈 요약 정보의 페이지 응답 DTO
   */
  Page<MemeResponseDto.ReadSummaryOneDto> readSummaryPage(int page, int size, int year, int quarter);

  /**
   * 관리자 권한으로 여러 밈을 일괄 삭제합니다.
   * <p>
   * 밈 ID 리스트와 삭제 사유를 받아, 해당 밈들을 일괄 삭제합니다.
   * 존재하지 않는 밈, 권한 등은 내부에서 예외 처리됩니다.
   * </p>
   *
   * @param deletedMemeIdList 삭제할 밈 ID 리스트
   * @param deletedMessage    삭제 사유(로그 등)
   * @param admin             관리자 유저 엔티티
   */
  void deleteMany(List<Long> deletedMemeIdList, String deletedMessage, User admin);

  /**
   * 밈 ID로 밈 엔티티를 직접 조회합니다.
   * <p>
   * 내부 로직(상세, 수정, 삭제 등)에서 활용되는 메서드입니다.
   * </p>
   *
   * @param memeId 조회할 밈의 고유 식별자
   * @return 밈 엔티티 객체
   */
  Meme findById(Long memeId);
}
