package com.example.memecommerceback.domain.hashtags.service;

import com.example.memecommerceback.domain.hashtags.dto.HashtagRequestDto;
import com.example.memecommerceback.domain.hashtags.dto.HashtagResponseDto;
import com.example.memecommerceback.domain.hashtags.dto.HashtagResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 해시태그 관리를 위한 서비스 인터페이스입니다.
 * <p>
 * 해시태그의 생성, 수정, 삭제 및 조회 등 핵심 비즈니스 로직을 정의합니다. 관리자의 해시태그 관리 및 일반 사용자 검색, 자동완성 등에도 활용될 수 있습니다.
 * </p>
 *
 * @author MemeCommerce Team
 * @version 1.0
 * @since 1.0
 */
public interface HashtagServiceV1 {

  /**
   * 해시태그(여러 개)를 생성합니다.
   * <p>
   * 해시태그명 리스트를 받아 한 번에 여러 해시태그를 등록합니다. 욕설/중복 검증, 유효성 검사 등을 포함할 수 있습니다.
   * </p>
   *
   * @param requestDto 생성할 해시태그명 리스트를 포함한 요청 DTO
   * @return 생성된 해시태그 정보 및 생성 시각을 담은 응답 DTO
   */
  HashtagResponseDto.CreateDto create(HashtagRequestDto.CreateDto requestDto);

  /**
   * 해시태그의 이름을 수정합니다.
   * <p>
   * 지정된 해시태그 ID의 이름을 새로운 값으로 변경합니다. 욕설/중복 검증 등도 내부에서 처리될 수 있습니다.
   * </p>
   *
   * @param hashtagId 수정할 해시태그의 고유 식별자
   * @param name      새 해시태그명
   * @return 수정된 해시태그 정보를 포함한 응답 DTO
   */
  HashtagResponseDto.UpdateOneDto updateOne(Long hashtagId, String name);

  /**
   * 여러 해시태그를 일괄 삭제합니다.
   * <p>
   * 해시태그 ID 리스트를 받아 해당하는 해시태그를 모두 삭제합니다. 존재하지 않는 ID, 연관 무결성 등도 내부에서 처리됩니다.
   * </p>
   *
   * @param requestDto 삭제할 해시태그 ID 리스트를 포함한 요청 DTO
   */
  void delete(HashtagRequestDto.DeleteDto requestDto);

  /**
   * 해시태그 ID로 해시태그 엔티티를 조회합니다.
   * <p>
   * 내부적으로 사용되는 메소드로, 해시태그 엔티티를 직접 반환합니다. 상세 정보, 수정/삭제 등 다양한 비즈니스 로직에서 활용됩니다.
   * </p>
   *
   * @param hashtagId 조회할 해시태그의 고유 식별자
   * @return 해시태그 엔티티 객체
   */
  Hashtag findById(Long hashtagId);

  Page<ReadOneDto> readPage(int page, int size);

  /**
   * 해시테그 ID 리스트로 여러 해시테그 엔티티를 조회합니다.
   * <p>
   * 내부적으로 사용되는 메소드로, 여러 해시테그 엔티티 객체를 한 번에 조회합니다.
   * 상품-해시테그 연관관계 관리 등에서 활용됩니다.
   * </p>
   *
   * @param hashtagIdList 조회할 해시테그 ID 리스트
   * @return 해시테그 엔티티 리스트
   */
  List<Hashtag> findAllById(List<Long> hashtagIdList);
}
