package com.example.memecommerceback.domain.categories.service;

import com.example.memecommerceback.domain.categories.dto.CategoryRequestDto;
import com.example.memecommerceback.domain.categories.dto.CategoryResponseDto;
import com.example.memecommerceback.domain.categories.entity.Category;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 카테고리 관리를 위한 서비스 인터페이스
 * <p>
 * 카테고리의 생성, 수정, 삭제 및 조회 기능을 제공합니다.
 * 관리자 권한을 필요로 하며, 카테고리와 연관된 다양한 비즈니스 로직을 담당합니다.
 * </p>
 *
 * @author MemeCommerce Team
 * @version 1.0
 * @since 1.0
 */
public interface CategoryServiceV1 {

  /**
   * 새로운 카테고리(여러 개)를 생성합니다.
   * <p>
   * 카테고리 이름 리스트를 받아 한 번에 여러 개의 카테고리를 생성합니다.
   * 중복 카테고리명 검사, 유효성 검사 등을 수행합니다.
   * </p>
   *
   * @param requestDto 생성할 카테고리 이름 리스트를 포함한 요청 DTO
   * @return 생성된 카테고리 정보 리스트 및 생성 시각을 포함한 응답 DTO
   */
  CategoryResponseDto.CreateDto create(CategoryRequestDto.CreateDto requestDto);

  /**
   * 카테고리 하나의 이름을 수정합니다.
   * <p>
   * 지정된 카테고리 ID의 이름을 새로운 값으로 변경합니다.
   * 이름 유효성 검사, 중복 검사 등을 수행합니다.
   * </p>
   *
   * @param categoryId 수정할 카테고리의 고유 식별자
   * @param name 새로운 카테고리명
   * @return 수정된 카테고리 정보를 포함한 응답 DTO
   */
  CategoryResponseDto.UpdateOneDto updateOne(Long categoryId, String name);

  /**
   * 여러 카테고리를 일괄 삭제합니다.
   * <p>
   * 카테고리 ID 리스트를 받아 해당하는 모든 카테고리를 한 번에 삭제합니다.
   * 존재하지 않는 카테고리, 연관 데이터 무결성 등도 처리합니다.
   * </p>
   *
   * @param requestDto 삭제할 카테고리 ID 리스트를 포함한 요청 DTO
   */
  void delete(CategoryRequestDto.DeleteDto requestDto);

  /**
   * 카테고리 ID로 카테고리 엔티티를 조회합니다.
   * <p>
   * 내부적으로 사용되는 메소드로, 카테고리 엔티티 객체를 직접 반환합니다.
   * 다른 서비스나 내부 로직에서 카테고리 엔티티가 필요할 때 사용됩니다.
   * </p>
   *
   * @param categoryId 조회할 카테고리의 고유 식별자
   * @return 카테고리 엔티티 객체
   */
  Category findById(Long categoryId);

  /**
   * 카테고리 페이지(목록) 정보를 조회합니다.
   * <p>
   * 지정한 페이지 번호와 크기에 따라 카테고리 목록을 페이징하여 반환합니다.
   * 일반적으로 관리자/사용자 모두의 목록, 검색, 자동완성 등 다양한 용도로 활용될 수 있습니다.
   * </p>
   *
   * @param page 조회할 페이지 번호 (0부터 시작)
   * @param size 한 페이지에 포함할 카테고리 개수
   * @return 카테고리 목록 페이지(Page) 객체 (각 항목은 ReadOneDto로 반환)
   */
  Page<CategoryResponseDto.ReadOneDto> readPage(int page, int size);
  /**
   * 카테고리 ID 리스트로 여러 카테고리 엔티티를 조회합니다.
   * <p>
   * 내부적으로 사용되는 메소드로, 여러 카테고리 엔티티 객체를 한 번에 조회합니다.
   * 상품-카테고리 연관관계 관리 등에서 활용됩니다.
   * </p>
   *
   * @param categoryIdList 조회할 카테고리 ID 리스트
   * @return 카테고리 엔티티 리스트
   */
  List<Category> findAllById(List<Long> categoryIdList);
}
