package com.example.memecommerceback.domain.products.service;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.products.dto.ProductRequestDto;
import com.example.memecommerceback.domain.products.dto.ProductResponseDto;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.exception.ProductCustomException;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.exception.PageCustomException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품 관리를 위한 서비스 인터페이스
 * <p>
 * 상품의 등록, 조회, 수정, 삭제 및 상태 관리 기능을 제공합니다. 사용자 권한(일반 사용자, 판매자, 관리자)에 따른 차별화된 서비스를 제공합니다.
 * </p>
 *
 * @author MemeCommerce Team
 * @version 1.0
 * @since 1.0
 */
public interface ProductServiceV1 {

  /**
   * 새로운 상품을 등록합니다.
   * <p>
   * 상품 정보와 이미지 파일들을 받아 새로운 상품을 시스템에 등록합니다. 상품명과 설명의 유사성 검사, 욕설 필터링, 판매 일정 검증 등을 수행합니다.
   * </p>
   *
   * @param requestDto       상품 등록 요청 데이터 (상품명, 설명, 가격, 재고, 판매 일정 등)
   * @param productImageList 상품 이미지 파일들 (최대 5개)
   * @param loginUser        로그인한 사용자 (판매자 권한 필요)
   * @return 등록된 상품 정보를 포함한 응답 DTO
   * @throws ProductCustomException 상품명/설명 유사성 검사 실패, 욕설 포함, 판매 일정 오류 등의 경우
   * @throws FileCustomException    이미지 파일 업로드 실패 또는 최대 개수 초과 시
   */
  ProductResponseDto.RegisterOneDto registerOne(
      ProductRequestDto.RegisterOneDto requestDto,
      List<MultipartFile> productImageList, User loginUser);

  /**
   * 관리자가 상품의 상태를 변경합니다.
   * <p>
   * 관리자 권한으로 상품의 상태를 변경합니다. 모든 상태 전환이 가능하며, 필요시 판매 일정도 함께 업데이트됩니다.
   * </p>
   *
   * @param productId       상태를 변경할 상품의 고유 식별자
   * @param requestedStatus 변경하고자 하는 상품 상태 문자열
   * @param Admin           관리자 사용자 (ROLE_ADMIN 권한 필요)
   * @return 상태 변경된 상품 정보를 포함한 응답 DTO
   * @throws ProductCustomException 상품을 찾을 수 없거나, 알 수 없는 상태값, 동일한 상태 요청 시
   */
  ProductResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      UUID productId, String requestedStatus, User Admin);

  /**
   * 판매자가 자신의 상품 정보를 수정합니다.
   * <p>
   * 판매자가 소유한 상품의 정보를 수정하고 이미지를 변경할 수 있습니다. 상품명과 설명의 유사성 검사, 욕설 필터링, 상태 전환 규칙 검증을 수행합니다.
   * </p>
   *
   * @param productId         수정할 상품의 고유 식별자
   * @param requestDto        수정할 상품 정보 (상품명, 설명, 가격, 재고, 상태, 판매 일정 등)
   * @param multipartFileList 새로운 상품 이미지 파일들 (기존 이미지는 모두 교체됨)
   * @param seller            판매자 사용자 (상품 소유자여야 함)
   * @return 수정된 상품 정보를 포함한 응답 DTO
   * @throws ProductCustomException 상품 소유자가 아니거나, 허용되지 않는 상태 전환, 유사성 검사 실패 등의 경우
   */
  ProductResponseDto.UpdateOneDto updateOneBySeller(
      UUID productId, ProductRequestDto.UpdateOneDto requestDto,
      List<MultipartFile> multipartFileList, User seller);

  /**
   * 상품 상세 정보를 조회합니다.
   * <p>
   * 주어진 상품 ID에 해당하는 상품의 상세 정보를 조회합니다. 상품 정보, 이미지, 좋아요 수, 조회수 등을 포함합니다.
   * </p>
   *
   * @param productId 조회할 상품의 고유 식별자
   * @return 상품 상세 정보를 포함한 응답 DTO
   * @throws ProductCustomException 상품을 찾을 수 없는 경우
   */
  ProductResponseDto.ReadOneDto readOne(UUID productId);

  /**
   * 모든 사용자가 접근 가능한 상품 목록을 페이지네이션으로 조회합니다.
   * <p>
   * 일반 사용자가 볼 수 있는 상품들(숨김, 대기, 거절 상태 제외)을 페이지네이션과 정렬 기능과 함께 조회합니다.
   * </p>
   *
   * @param page       페이지 번호 (0부터 시작)
   * @param size       페이지당 항목 수
   * @param sortList   정렬 기준 목록 (createdAt, likeCount, viewCount, price 사용 가능)
   * @param statusList 필터링할 상품 상태 목록 (숨김, 대기, 거절 상태는 제외됨)
   * @return 페이지네이션된 상품 목록
   * @throws ProductCustomException 허용되지 않는 상태로 필터링 시도 시
   * @throws PageCustomException    잘못된 정렬 필드 사용 시
   */
  Page<ProductResponseDto.ReadOneDto> readPageByAll(
      int page, int size, List<String> sortList, List<String> statusList);

  /**
   * 판매자가 자신의 상품 목록을 페이지네이션으로 조회합니다.
   * <p>
   * 로그인한 판매자가 소유한 모든 상품을 상태에 관계없이 조회할 수 있습니다. 페이지네이션과 정렬, 상태 필터링 기능을 제공합니다.
   * </p>
   *
   * @param page       페이지 번호 (0부터 시작)
   * @param size       페이지당 항목 수
   * @param sortList   정렬 기준 목록 (createdAt, likeCount, viewCount, price 사용 가능)
   * @param statusList 필터링할 상품 상태 목록
   * @param seller     판매자 사용자
   * @return 페이지네이션된 판매자 상품 목록
   * @throws PageCustomException 잘못된 정렬 필드 사용 시
   */
  Page<ProductResponseDto.ReadOneDto> readPageBySeller(
      int page, int size, List<String> sortList,
      List<String> statusList, User seller);

  /**
   * 관리자가 모든 상품 목록을 페이지네이션으로 조회합니다.
   * <p>
   * 관리자는 시스템의 모든 상품을 상태에 관계없이 조회할 수 있습니다. 페이지네이션과 정렬, 상태 필터링 기능을 제공합니다.
   * </p>
   *
   * @param page       페이지 번호 (0부터 시작)
   * @param size       페이지당 항목 수
   * @param sortList   정렬 기준 목록 (createdAt, likeCount, viewCount, price 사용 가능)
   * @param statusList 필터링할 상품 상태 목록
   * @return 페이지네이션된 전체 상품 목록
   * @throws PageCustomException 잘못된 정렬 필드 사용 시
   */
  Page<ProductResponseDto.ReadOneDto> readPageByAdmin(
      int page, int size, List<String> sortList, List<String> statusList);

  /**
   * 여러 상품을 일괄 삭제합니다.
   * <p>
   * 관리자는 모든 상품을, 판매자는 자신이 소유한 상품만 삭제할 수 있습니다. 상품과 연관된 이미지 파일들도 함께 삭제됩니다.
   * </p>
   *
   * @param requestDto 삭제할 상품 ID 목록을 포함한 요청 DTO
   * @param loginUser  로그인한 사용자 (관리자 또는 상품 소유자)
   * @throws ProductCustomException 상품을 찾을 수 없거나, 삭제 권한이 없는 경우
   */
  void deleteMany(ProductRequestDto.DeleteDto requestDto, User loginUser);

  /**
   * 판매 시작일이 된 상품들의 상태를 판매중(ON_SALE)으로 업데이트합니다.
   * <p>
   * 스케줄러에 의해 자동으로 호출되는 메소드입니다. 재판매 예정(RESALE_SOON) 상태의 상품 중 판매 시작일이 현재 시간 이전인 상품들을 판매중(ON_SALE) 상태로
   * 변경합니다.
   * </p>
   */
  void updateOnSaleStatus();

  /**
   * 판매 종료일이 지난 상품들의 상태를 숨김(HIDDEN)으로 업데이트합니다.
   * <p>
   * 스케줄러에 의해 자동으로 호출되는 메소드입니다. 판매 종료일이 현재 시간 이전인 상품들을 숨김(HIDDEN) 상태로 변경합니다.
   * </p>
   */
  void updateHiddenStatus();

  /** 새로운 이모지 팩 상품을 등록합니다.
  * <p>
  * 이모지 팩 상품 정보와 메인 상품 이미지, 이모지 이미지들을 받아 새로운 이모지 팩을 시스템에 등록합니다.
  * </p>
  *
  * @param requestDto 이모지 팩 등록 요청 데이터
  * @param mainProductImageList 메인 상품 이미지 파일들
  * @param emojiImageList 이모지 이미지 파일들
  * @param seller 판매자 사용자
  * @return 등록된 이모지 팩 정보를 포함한 응답 DTO
  * @throws ProductCustomException 상품 등록 실패 시
  * @throws FileCustomException 이미지 파일 업로드 실패 시
  */
  ProductResponseDto.EmojiPackDto registerEmojiPack(
      ProductRequestDto.EmojiPackDto requestDto,
      List<MultipartFile> mainProductImageList, List<MultipartFile> emojiImageList,
      User seller);

  /**
  * 이모지 팩 상품을 수정합니다.
  * <p>
  * 판매자가 소유한 이모지 팩 상품의 정보를 수정합니다.
  * </p>
  *
  * @param productId 수정할 상품의 고유 식별자
  * @param requestDto 수정할 이모지 팩 정보
  * @param mainProductImageList 새로운 메인 상품 이미지 파일들
  * @param seller 판매자 사용자
  * @return 수정된 이모지 팩 정보를 포함한 응답 DTO
  * @throws ProductCustomException 상품 수정 실패 시
  */
  ProductResponseDto.EmojiPackDto updateEmojiPack(
      UUID productId, ProductRequestDto.UpdateEmojiPackDto requestDto,
      List<MultipartFile> mainProductImageList, User seller);

  /**
   * 상품 ID로 상품 엔티티를 조회합니다.
   * <p>
   * 내부적으로 사용되는 메소드로, 상품 엔티티 객체를 직접 반환합니다. 다른 서비스나 내부 로직에서 상품 엔티티가 필요할 때 사용됩니다.
   * </p>
   *
   * @param productId 조회할 상품의 고유 식별자
   * @return 상품 엔티티 객체
   * @throws ProductCustomException 상품을 찾을 수 없는 경우
   */
  Product findById(UUID productId);

  /**
   * 소유자 ID로 모든 상품을 조회합니다.
   * <p>
   * 특정 사용자가 소유한 모든 상품 목록을 반환합니다. 내부적으로 사용되는 메서드로, 사용자 경로 변경이나
   * 기타 비즈니스 로직에서 특정 소유자의 상품 목록이 필요할 때 사용됩니다.
   * </p>
   *
   * @param ownerId 조회할 소유자의 고유 식별자
   * @return 소유자가 소유한 상품 목록 (소유한 상품이 없는 경우 빈 리스트 반환)
   */
  List<Product> findAllByOwnerId(UUID ownerId);
}
