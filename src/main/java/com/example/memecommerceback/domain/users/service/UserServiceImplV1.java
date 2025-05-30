package com.example.memecommerceback.domain.users.service;

import com.example.memecommerceback.domain.files.entity.File;
import com.example.memecommerceback.domain.files.entity.FileType;
import com.example.memecommerceback.domain.files.service.FileServiceV1;
import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.users.converter.UserConverter;
import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.SellerStatus;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.entity.UserRole;
import com.example.memecommerceback.domain.users.exception.UserCustomException;
import com.example.memecommerceback.domain.users.exception.UserExceptionCode;
import com.example.memecommerceback.domain.users.repository.UserRepository;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserServiceV1 {

  private final FileServiceV1 fileService;
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
    String currentNickname = user.getNickname();
    String afterNickname = requestDto.getNickname();

    // 1. 이전에 닉네임의 설정이 안되어있는 경우
    if(loginUser.getNickname() == null){
      throw new UserCustomException(UserExceptionCode.NEED_TO_REGISTER_NICKNAME);
    }

    if(userRepository.existsByNickname(afterNickname)){
      throw new UserCustomException(UserExceptionCode.EXIST_NICKNAME);
    }
    // 2. deleteProfileImage는 프로필 삭제 플래그-> null or 현상 유지
    // 프로필 삭제를 요청하지 않고 profileImage를 넣엇을 때 -> 그대로 동작
    // 프로필 삭제를 요청하지 않고 profileImage를 넣었을 때 -> 그대로 동작
    // 프로필 삭제를 요청하고 profileImage를 넣었을 때 -> 예외
    // 프로필 삭제를 요청하고 profileImage를 안 넣었을 때 -> 그대로 동작
    if(requestDto.isDeleteProfileImage()
        && profileImage != null && !profileImage.isEmpty()){
      throw new UserCustomException(
          UserExceptionCode.CONFLICT_IMAGE_UPDATE_AND_DELETE);
    }

    // 3. 프로필 이미지 삭제 선택 -> 삭제를 원함.
    if (requestDto.isDeleteProfileImage()) {
      if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()
          && afterNickname.equals(currentNickname)) {
        imageService.deleteProfileImage(user.getNickname());
        user.updateProfileImage(null);
      }
    }
    // 4. 닉네임 변경 + 이미지 변경
    else if (!afterNickname.equals(currentNickname)
        && profileImage != null && !profileImage.isEmpty()) {
      String changeProfileImageUrl
          = imageService.reloadImageAndChangeUserPath(
              user, profileImage, currentNickname, afterNickname);

      user.updateProfile(
          changeProfileImageUrl, afterNickname,
          requestDto.getAddress(), requestDto.getContact());
      // imageService에서 1차 캐시를 초기화하는 로직이 있어서, save 해야함.
      userRepository.save(user);
    }
    // 5. 닉네임만 변경
    else if (!afterNickname.equals(currentNickname)
        && (profileImage == null || profileImage.isEmpty())) {
      String changeProfileImageUrl
          = imageService.changeUserPath(currentNickname, afterNickname);
      user.updateProfile(
          changeProfileImageUrl, afterNickname,
          requestDto.getAddress(), requestDto.getContact());
      userRepository.save(user);
    }
    // 6. 이미지 변경만
    else if (afterNickname.equals(currentNickname)
        && profileImage != null && !profileImage.isEmpty()) {
      imageService.deleteS3Object(user.getProfileImage());
      String changeProfileImageUrl
          = imageService.uploadAndRegisterUserProfileImage(profileImage, user);
      user.updateProfile(
          changeProfileImageUrl, afterNickname,
          requestDto.getAddress(), requestDto.getContact());
    }
    // 7. 모두 변경하지 않을 때 (연락처, 주소 등만 변경)
    else {
      // 연락처, 주소 등만 업데이트
      user.updateContactAndAddress(
          requestDto.getContact(),
          requestDto.getAddress()
      );
    }

    return UserConverter.toUpdateProfileDto(user);
  }

  @Override
  @Transactional
  public UserResponseDto.IsAvailableNicknameDto isAvailableNickname(
      String requestedNickname) {
    boolean isAvailable
        = !userRepository.existsByNickname(requestedNickname);
    profanityFilterService.validateNoProfanity(requestedNickname);
    return UserConverter.toIsAvailableNicknameDto(
        requestedNickname, isAvailable);
  }

  @Override
  @Transactional
  public UserResponseDto.UpdateProfileDto updateNickname(
      String requestedNickname, User loginUser) {
    User user = findById(loginUser.getId());
    boolean isAvailable
        = !userRepository.existsByNickname(requestedNickname);
    if (!isAvailable) {
      throw new UserCustomException(UserExceptionCode.REQUEST_DUPLICATE_NICKNAME);
    }
    profanityFilterService.validateNoProfanity(requestedNickname);
    user.updateNickname(requestedNickname);
    return UserConverter.toUpdateProfileDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponseDto.ReadProfileDto readProfile(User loginUser) {
    User user = findById(loginUser.getId());
    return UserConverter.toReadProfileDto(user);
  }

  @Override
  @Transactional
  public void deleteOne(UUID userId, User loginUser) {
    User user = findById(userId);
    if (!loginUser.getRole().equals(UserRole.ADMIN)
        && !loginUser.getId().equals(userId)) {
      throw new UserCustomException(UserExceptionCode.ONLY_SELF_OR_ADMIN_CAN_DELETE);
    }

    if (user.getProfileImage() != null) {
      imageService.deleteS3Object(user.getProfileImage());
    }
    fileService.deleteUserWithFiles(userId);
    userRepository.deleteById(userId);
  }

  @Override
  @Transactional
  public UserResponseDto.UpdateRoleDto updateRoleSellerByUser(
      List<MultipartFile> multipartFileList, User loginUser) {
    User user
        = userRepository.findByIdAndRole(
        loginUser.getId(), loginUser.getRole()).orElseThrow(
        () -> new UserCustomException(UserExceptionCode.NOT_FOUND));

    if (!user.getSellerStatus().equals(SellerStatus.NONE)) {
      throw new UserCustomException(
          UserExceptionCode.ALREADY_COMPLETED_OR_PENDING_STATUS);
    }

    List<File> fileList
        = fileService.uploadUserFileList(multipartFileList, user);

    user.updateSellerStatus(SellerStatus.PENDING);

    return UserConverter.toUpdateRoleDto(user, fileList);
  }

  @Override
  @Transactional
  public UserResponseDto.UpdateRoleDto updateRoleByAdmin(
      UUID userId, User loginUser, String role) {
    User user = findById(userId);
    if(user.getRole().equals(UserRole.ADMIN)){
      throw new UserCustomException(UserExceptionCode.CANNOT_CHANGE_ADMIN_ROLE);
    }

    if (SellerStatus.getProcessedSellerStatus()
        .contains(user.getSellerStatus())) {
      throw new UserCustomException(
          UserExceptionCode.ALREADY_COMPLETED_STATUS);
    }

    UserRole userRole = UserRole.fromAuthority(role);
    user.updateRole(userRole);

    // 유저에서 '판매자'로 권한이 되기 위한 파일 리스트를 가지고
    // 있을 필요가 없을 것이라 생각하여 단방향 연관관계로 인한
    List<File> userFileList
        = fileService.findAllByOwnerIdAndFileType(
            user.getId(), FileType.SELLER_CERTIFICATE);

    return UserConverter.toUpdateRoleDto(user, userFileList);
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(UUID loginUserId) {
    return userRepository.findById(loginUserId).orElseThrow(
        () -> new UserCustomException(UserExceptionCode.NOT_FOUND));
  }
}
