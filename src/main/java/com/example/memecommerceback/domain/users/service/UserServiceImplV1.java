package com.example.memecommerceback.domain.users.service;

import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.users.converter.UserConverter;
import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.entity.UserRole;
import com.example.memecommerceback.domain.users.exception.UserCustomException;
import com.example.memecommerceback.domain.users.exception.UserExceptionCode;
import com.example.memecommerceback.domain.users.repository.UserRepository;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserServiceV1 {

  private final ImageServiceV1 imageService;
  private final ProfanityFilterService profanityFilterService;

  private final UserRepository userRepository;

  @Override
  @Transactional
  public void save(User user) {
    userRepository.save(user);
  }

  @Override
  @Transactional
  public Optional<User> findByContactFetchOAuth(String contact) {
    return userRepository.findByContactFetchOAuth(contact);
  }

  @Override
  @Transactional
  public UserResponseDto.UpdateProfileDto updateProfile(
      UserRequestDto.UpdateProfileDto requestDto,
      MultipartFile profileImage, User loginUser) {

    User user = findById(loginUser.getId());
    String beforeNickname = user.getNickname();

    String profileImageUrl = null;

    // 1. 새 프로필 이미지가 있는 경우: 기존 이미지 삭제 후 새 이미지 업로드
    if (profileImage != null) {
      if (user.getNickname() == null) {
        throw new UserCustomException(
            UserExceptionCode.NEED_TO_REGISTER_NICKNAME);
      }

      if (requestDto.getNickname() != null
          && !user.getNickname().equals(requestDto.getNickname())) {
        user.updateNickname(requestDto.getNickname());
      }

      profileImageUrl = getProfileImageUrl(profileImage, user);

      if (profileImageUrl != null && user.getProfileImage() != null) {
        imageService.deleteProfile(user.getId());
      }
    }

    // 2. 닉네임만 변경 & 새 프로필 이미지는 없는 경우: 기존 이미지 경로 이동, 새 URL 할당
    String newProfileUrl = null;
    if (profileImage == null
        && requestDto.getNickname() != null
        && !requestDto.getNickname().equals(beforeNickname)) {
      newProfileUrl = imageService.changeProfilePath(
          profileImage, beforeNickname, requestDto.getNickname());
    }

    // 3. 유저 정보 업데이트 (가장 우선: 새 업로드 > 경로 이동 > 기존값)
    user.updateProfile(
        requestDto.getContact(),
        requestDto.getNickname(),
        requestDto.getAddress(),
        profileImageUrl != null ? profileImageUrl :
            (newProfileUrl != null ? newProfileUrl : user.getProfileImage())
    );

    return UserConverter.toUpdateProfileDto(user);
  }

  @Override
  @Transactional
  public UserResponseDto.IsAvailableNicknameDto isAvailableNickname(
      String requestedNickname){
    boolean isAvailable
        = !userRepository.existsByNickname(requestedNickname);
    profanityFilterService.validateNoProfanity(requestedNickname);
    return UserConverter.toIsAvailableNicknameDto(
        requestedNickname, isAvailable);
  }

  /**
   * 로그인한 사용자의 닉네임을 변경합니다.
   *
   * 닉네임이 이미 사용 중이거나 부적절한 단어가 포함된 경우 예외를 발생시킵니다.
   *
   * @param requestedNickname 변경할 닉네임
   * @param loginUser 닉네임을 변경할 로그인 사용자
   * @return 변경된 프로필 정보를 담은 DTO
   * @throws UserCustomException 닉네임이 중복되었거나 부적절할 때 발생
   */
  @Override
  @Transactional
  public UserResponseDto.UpdateProfileDto updateNickname(
      String requestedNickname, User loginUser){
    User user = findById(loginUser.getId());
    boolean isAvailable
        = !userRepository.existsByNickname(requestedNickname);
    if(!isAvailable){
      throw new UserCustomException(UserExceptionCode.REQUEST_DUPLICATE_NICKNAME);
    }
    profanityFilterService.validateNoProfanity(requestedNickname);
    user.updateNickname(requestedNickname);
    return UserConverter.toUpdateProfileDto(user);
  }

  /**
   * 로그인한 사용자의 프로필 정보를 조회합니다.
   *
   * @param loginUser 현재 로그인한 사용자
   * @return 사용자의 프로필 정보를 담은 ReadProfileDto 객체
   */
  @Override
  @Transactional(readOnly = true)
  public UserResponseDto.ReadProfileDto readProfile(User loginUser) {
    User user = findById(loginUser.getId());
    return UserConverter.toReadProfileDto(user);
  }

  /**
   * 지정된 사용자를 삭제합니다.
   *
   * 관리자이거나 본인인 경우에만 삭제가 허용되며, 프로필 이미지가 존재하면 함께 삭제됩니다.
   *
   * @param userId 삭제할 사용자의 UUID
   * @param loginUser 현재 로그인한 사용자
   * @throws UserCustomException 삭제 권한이 없는 경우 발생
   */
  @Override
  @Transactional
  public void deleteOne(UUID userId, User loginUser) {
    if(!loginUser.getRole().equals(UserRole.ADMIN)
        && !loginUser.getId().equals(userId)){
      throw new UserCustomException(UserExceptionCode.ONLY_SELF_OR_ADMIN_CAN_DELETE);
    }
    if (userRepository.findById(userId).isPresent() &&
        userRepository.findById(userId).get().getProfileImage() != null) {
      imageService.deleteProfile(userId);
    }

    userRepository.deleteById(userId);
  }

  /**
   * 주어진 ID로 사용자를 조회합니다.
   *
   * @param loginUserId 조회할 사용자의 UUID
   * @return 해당 UUID에 해당하는 사용자 엔티티
   * @throws UserCustomException 사용자를 찾을 수 없는 경우 발생
   */
  @Override
  @Transactional(readOnly = true)
  public User findById(UUID loginUserId){
    return userRepository.findById(loginUserId).orElseThrow(
        ()-> new UserCustomException(UserExceptionCode.NOT_FOUND));
  }

  private String getProfileImageUrl(MultipartFile profileImage, User user) {
    if (profileImage == null) {
      return null; // 이미지가 없으면 null 반환
    }

    return imageService.uploadAndRegisterUserProfileImage(profileImage, user);
  }
}
