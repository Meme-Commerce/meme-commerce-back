package com.example.memecommerceback.domain.images.service;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.images.entity.Image;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3ImageResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 *
 * <p>사용자 프로필 이미지, 제품 이미지, 이모지 이미지의 업로드, 변경, 삭제 등의
 * 기능을 제공하며, AWS S3와의 연동을 통해 이미지 파일을 관리합니다.</p>
 *
 * @author meme-commerce-team
 * @version 1.0
 * @since 1.0
 */
public interface ImageServiceV1 {

  /**
   * 사용자 프로필 이미지를 업로드하고 데이터베이스에 등록합니다.
   *
   * @param profileImage 업로드할 프로필 이미지 파일
   * @param user 프로필 이미지를 등록할 사용자
   * @return 업로드된 이미지의 URL
   * @throws IllegalArgumentException 프로필 이미지가 null이거나 빈 파일인 경우
   * @throws RuntimeException S3 업로드 실패 시
   */
  String uploadAndRegisterUserProfileImage(
      MultipartFile profileImage, User user);

  /**
   * 특정 제품의 모든 이미지를 삭제합니다.
   *
   * @param productId 삭제할 이미지가 속한 제품의 ID
   * @param userId 삭제 요청을 하는 사용자의 ID
   * @throws IllegalArgumentException productId 또는 userId가 null인 경우
   * @throws RuntimeException 이미지 삭제 실패 시
   */
  void deleteProductImageList(UUID productId, UUID userId);

  /**
   * 제품 이미지 목록을 S3에 업로드합니다.
   *
   * @param productImageList 업로드할 제품 이미지 파일 목록
   * @param nickname 업로드하는 사용자의 닉네임
   * @param productId 이미지가 속할 제품의 ID
   * @return 업로드된 이미지 정보 목록
   * @throws IllegalArgumentException 이미지 목록이 null이거나 빈 경우
   * @throws RuntimeException S3 업로드 실패 시
   */
  List<S3ImageResponseDto> uploadProductImageList(
      List<MultipartFile> productImageList, String nickname, UUID productId);

  /**
   * S3에서 특정 URL의 객체를 삭제합니다.
   *
   * @param url 삭제할 S3 객체의 URL
   * @throws IllegalArgumentException url이 null이거나 빈 문자열인 경우
   * @throws RuntimeException S3 삭제 실패 시
   */
  void deleteS3Object(String url);

  /**
   * S3에 업로드된 이미지 정보를 엔티티로 변환하고 데이터베이스에 저장합니다.
   *
   * @param uploadedImageList S3에 업로드된 이미지 정보 목록
   * @param loginUser 이미지를 업로드한 사용자
   * @param product 이미지가 속할 제품
   * @return 저장된 이미지 엔티티 목록
   * @throws IllegalArgumentException 필수 파라미터가 null인 경우
   * @throws RuntimeException 데이터베이스 저장 실패 시
   */
  List<Image> toEntityProductListAndSaveAll(
      List<S3ImageResponseDto> uploadedImageList, User loginUser, Product product);

  /**
   * 이모지 이미지를 업로드하고 데이터베이스에 등록합니다.
   *
   * @param multipartFileList 업로드할 이모지 이미지 파일 목록
   * @param seller 이모지를 등록하는 판매자
   * @param productId 이모지가 속할 제품의 ID
   * @return 등록된 이모지 이미지 엔티티 목록
   * @throws IllegalArgumentException 필수 파라미터가 null인 경우
   * @throws RuntimeException 이모지 이미지 업로드 실패 시
   */
  List<Image> uploadEmojiImage(
      List<MultipartFile> multipartFileList, User seller, UUID productId);

  /**
   * 사용자의 닉네임 변경에 따라 S3 내 이미지 경로를 변경합니다.
   *
   * @param beforeNickname 변경 전 닉네임
   * @param afterNickname 변경 후 닉네임
   * @param productIdList 경로를 변경할 제품 ID 목록
   * @return 경로 변경 결과 URL
   * @throws IllegalArgumentException 닉네임이 null이거나 빈 문자열인 경우
   * @throws RuntimeException S3 경로 변경 실패 시
   */
  String changeUserPath(
      String beforeNickname, String afterNickname, List<UUID> productIdList);

  /**
   * 특정 사용자의 프로필 이미지를 삭제합니다.
   *
   * @param ownerNickname 프로필 이미지를 삭제할 사용자의 닉네임
   * @throws IllegalArgumentException ownerNickname이 null이거나 빈 문자열인 경우
   * @throws RuntimeException 프로필 이미지 삭제 실패 시
   */
  void deleteProfileImage(String ownerNickname);

  /**
   * 사용자의 이미지를 다시 로드하고 경로를 변경합니다.
   *
   * @param user 대상 사용자
   * @param profile 새로운 프로필 이미지 파일
   * @param beforeNickname 변경 전 닉네임
   * @param afterNickname 변경 후 닉네임
   * @param productIdList 경로를 변경할 제품 ID 목록
   * @return 변경된 프로필 이미지 URL
   * @throws IllegalArgumentException 필수 파라미터가 null인 경우
   * @throws RuntimeException 이미지 로드 또는 경로 변경 실패 시
   */
  String reloadImageAndChangeUserPath(
      User user, MultipartFile profile, String beforeNickname,
      String afterNickname, List<UUID> productIdList);

  /**
   * 기존 이모지의 이미지를 새로운 이미지로 변경합니다.
   *
   * @param emojiImage 새로운 이모지 이미지 파일
   * @param emoji 변경할 이모지 엔티티
   * @param emojiOwner 이모지 소유자
   * @return 변경된 이미지 엔티티
   * @throws IllegalArgumentException 필수 파라미터가 null인 경우
   * @throws RuntimeException 이모지 이미지 변경 실패 시
   */
  Image changeEmojiImage(
      MultipartFile emojiImage, Emoji emoji, User emojiOwner);

  /**
   * 특정 이모지 이미지를 삭제합니다.
   *
   * @param deleteImage 삭제할 이미지 엔티티
   * @throws IllegalArgumentException deleteImage가 null인 경우
   * @throws RuntimeException 이모지 이미지 삭제 실패 시
   */
  void deleteEmojiImage(Image deleteImage);
}