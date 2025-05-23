package com.example.memecommerceback.domain.users.service;

import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserServiceV1 {

  void save(User user);
  Optional<User> findByContactFetchOAuth(String contact);

  UserResponseDto.UpdateProfileDto updateProfile(
      UserRequestDto.UpdateProfileDto requestDto,
      MultipartFile profileImage, User loginUser);

  User findById(UUID loginUserId);

  UserResponseDto.IsAvailableNicknameDto isAvailableNickname(
      String requestedNickname);

  /**
       * 로그인한 사용자의 닉네임을 지정한 값으로 변경합니다.
       *
       * @param requestedNickname 변경할 닉네임
       * @param loginUser 닉네임을 변경할 로그인 사용자
       * @return 변경된 프로필 정보를 담은 DTO
       */
      UserResponseDto.UpdateProfileDto updateNickname(
      String requestedNickname, User loginUser);

  /**
 * 로그인한 사용자의 프로필 정보를 조회합니다.
 *
 * @param loginUser 프로필을 조회할 로그인 사용자
 * @return 사용자의 프로필 정보가 담긴 DTO
 */
UserResponseDto.ReadProfileDto readProfile(User loginUser);

  /**
 * 지정된 사용자 ID에 해당하는 사용자를 삭제합니다.
 *
 * @param userId 삭제할 사용자의 UUID
 * @param loginUser 현재 로그인한 사용자 정보
 */
void deleteOne(UUID userId, User loginUser);
}
