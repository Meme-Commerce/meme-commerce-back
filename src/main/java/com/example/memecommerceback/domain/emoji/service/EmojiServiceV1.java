package com.example.memecommerceback.domain.emoji.service;

import com.example.memecommerceback.domain.emoji.dto.EmojiResponseDto;
import com.example.memecommerceback.domain.emoji.dto.EmojiThumbnailResponseDto;
import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto.EmojiDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이모지 관리를 위한 서비스 인터페이스
 * <p>
 * 이모지 등록, 조회, 수정, 삭제 및 페이징 조회 기능을 제공합니다.
 * 주로 상품(이모지 팩)과 연관된 이모지 등록, 수정, 단일 이모지 관리 등의 기능을 담당합니다.
 * </p>
 *
 * @author MemeCommerce Team
 * @version 1.0
 * @since 1.0
 */
public interface EmojiServiceV1 {

  /**
   * 새로운 이모지 목록을 등록합니다.
   * <p>
   * 이모지 이미지 파일들과 이모지 설명 리스트를 받아, 주어진 상품(이모지 팩)에 여러 이모지를 일괄 등록합니다.
   * </p>
   *
   * @param emojiImageList         이모지 이미지 파일 목록
   * @param product                이모지를 등록할 상품(이모지 팩) 엔티티
   * @param seller                 이모지 등록을 요청한 판매자
   * @param emojiPackName          이모지 팩 이름
   * @param emojiDescriptionList   이모지 설명 리스트
   * @return 등록된 이모지 엔티티 목록
   */
  List<Emoji> register(
      List<MultipartFile> emojiImageList, Product product, User seller,
      String emojiPackName, List<EmojiDto> emojiDescriptionList);

  /**
   * 특정 상품(이모지 팩)에 등록된 모든 이모지를 조회합니다.
   *
   * @param productId 조회할 상품(이모지 팩) ID
   * @return 해당 상품에 등록된 이모지 엔티티 목록
   */
  List<Emoji> findAllByProductId(UUID productId);

  /**
   * 단일 이모지 정보를 수정합니다.
   * <p>
   * 이모지의 이름과 이미지를 수정할 수 있습니다. 이미지가 null이면 이름만 수정됩니다.
   * </p>
   *
   * @param emojiId    수정할 이모지의 고유 식별자
   * @param name       변경할 이모지 이름
   * @param emojiImage 변경할 이모지 이미지 파일 (null이면 이미지 변경 없음)
   * @param seller     이모지를 소유한 판매자
   * @return 수정된 이모지의 응답 DTO
   */
  EmojiResponseDto updateOne(
      Long emojiId, String name, MultipartFile emojiImage, User seller);

  /**
   * 단일 이모지를 삭제합니다.
   * <p>
   * 이모지의 소유자(판매자)만 삭제할 수 있습니다. 연관된 이미지 파일도 함께 삭제됩니다.
   * </p>
   *
   * @param emojiId 삭제할 이모지의 고유 식별자
   * @param seller  이모지의 소유자(판매자)
   */
  void deleteOne(Long emojiId, User seller);

  /**
   * 이모지 썸네일 목록을 페이지네이션으로 조회합니다.
   * <p>
   * 이모지 목록을 썸네일 정보와 함께 페이지 단위로 조회할 수 있습니다.
   * </p>
   *
   * @param page 조회할 페이지 번호 (0부터 시작)
   * @param size 페이지당 항목 수
   * @return 페이지네이션된 이모지 썸네일 응답 DTO 목록
   */
  Page<EmojiThumbnailResponseDto> readPage(int page, int size);

  /**
   * 단일 이모지의 상세 정보를 조회합니다.
   *
   * @param emojiId 조회할 이모지의 고유 식별자
   * @return 이모지 상세 정보를 포함한 응답 DTO
   */
  EmojiResponseDto readOne(Long emojiId);

  /**
   * 단일 이모지 하나를 조회합니다.
   *
   * @param emojiId 조회할 이모지의 고유 식별자
   * @return 식별자와 일치하는 이모지
   */
  Emoji findById(Long emojiId);
}
