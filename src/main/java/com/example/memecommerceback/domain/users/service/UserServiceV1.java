package com.example.memecommerceback.domain.users.service;

import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.entity.UserRole;
import com.example.memecommerceback.domain.users.exception.UserCustomException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserServiceV1 {

  /**
   * 사용자 정보를 저장합니다.
   *
   * @param user 저장할 사용자 정보
   */
  void save(User user);

  /**
   * 연락처로 사용자를 검색하고 OAuth 제공자 정보를 함께 가져옵니다.
   *
   * @param contact 검색할 사용자의 연락처
   * @return 검색된 사용자 정보 (Optional)
   */
  Optional<User> findByContactFetchOAuth(String contact);

  /**
   * 사용자 프로필 정보를 업데이트합니다.
   *
   * @param requestDto   업데이트할 프로필 정보
   * @param profileImage 업로드된 프로필 이미지 (null 가능)
   * @param loginUser    현재 로그인한 사용자
   * @return 업데이트된 사용자 프로필 정보
   */
  UserResponseDto.UpdateProfileDto updateProfile(
      UserRequestDto.UpdateProfileDto requestDto,
      MultipartFile profileImage, User loginUser);

  /**
   * 사용자 ID로 사용자 정보를 검색합니다.
   *
   * @param loginUserId 검색할 사용자의 ID
   * @return 검색된 사용자 정보
   */
  User findById(UUID loginUserId);

  /**
   * 요청된 닉네임의 사용 가능 여부를 확인합니다.
   *
   * @param requestedNickname 확인할 닉네임
   * @return 닉네임 사용 가능 여부 정보가 담긴 DTO
   */
  UserResponseDto.IsAvailableNicknameDto isAvailableNickname(
      String requestedNickname);

  /**
   * 사용자의 닉네임을 업데이트합니다.
   *
   * @param requestedNickname 새로운 닉네임
   * @param loginUser         현재 로그인한 사용자
   * @return 업데이트된 사용자 프로필 정보
   */
  UserResponseDto.UpdateProfileDto updateNickname(
      String requestedNickname, User loginUser);

  /**
   * 현재 로그인한 사용자의 프로필 정보를 조회합니다.
   *
   * @param loginUser 현재 로그인한 사용자
   * @return 사용자 프로필 정보
   */
  UserResponseDto.ReadProfileDto readProfile(User loginUser);

  /**
   * 사용자를 삭제합니다. 본인 또는 관리자만 삭제할 수 있습니다.
   *
   * @param userId    삭제할 사용자의 ID
   * @param loginUser 현재 로그인한 사용자
   */
  void deleteOne(UUID userId, User loginUser);

  /**
   * 사용자가 판매자 권한을 요청합니다. 판매자 인증에 필요한 서류를 업로드합니다.
   *
   * @param fileList  판매자 인증을 위해 업로드할 파일 목록 (사업자등록증 등)
   * @param loginUser 현재 로그인한 사용자
   * @return 업데이트된 사용자 역할 정보
   */
  UserResponseDto.UpdateRoleDto updateRoleSellerByUser(
      List<MultipartFile> fileList, User loginUser);

  /**
   * 관리자가 사용자의 역할을 변경합니다.
   *
   * @param userId    역할을 변경할 사용자의 ID
   * @param loginUser 현재 로그인한 관리자 사용자
   * @param role      변경할 역할 (문자열)
   * @return 업데이트된 사용자 역할 정보
   */
  UserResponseDto.UpdateRoleDto updateRoleByAdmin(
      UUID userId, User loginUser, String role);
}