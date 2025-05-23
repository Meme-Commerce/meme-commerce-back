package com.example.memecommerceback.domain.users.service;

import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.users.converter.UserConverter;
import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.exception.UserCustomException;
import com.example.memecommerceback.domain.users.exception.UserExceptionCode;
import com.example.memecommerceback.domain.users.repository.UserRepository;
import com.example.memecommerceback.global.jwt.JwtConstants;
import com.example.memecommerceback.global.redis.service.RefreshTokenServiceV1;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserServiceV1 {

  private final ImageServiceV1 imageService;
  private final UserRepository userRepository;
  private final RefreshTokenServiceV1 refreshTokenService;

  @Override
  @Transactional
  public void save(User user) {
    userRepository.save(user);
  }

  @Override
  @Transactional
  public List<User> findByContactFetchOAuth(String contact) {
    return userRepository.findByContactFetchOAuth(contact);
  }

  @Override
  @Transactional
  public UserResponseDto.UpdateProfileDto updateProfile(
      UserRequestDto.UpdateProfileDto requestDto,
      MultipartFile profileImage, User loginUser) {

    User user = findById(loginUser.getId());
    String beforeEmail = user.getEmail();
    String beforeNickname = user.getNickname();

    String profileImageUrl = null;

    // 1. 새 프로필 이미지가 있는 경우: 기존 이미지 삭제 후 새 이미지 업로드
    if (profileImage != null) {
      if (user.getProfileImage() != null) {
        imageService.deleteProfile(user.getId());
      }
      profileImageUrl = getProfileImageUrl(profileImage, user); // 새로 업로드된 URL
    }

    // 2. 닉네임만 변경 & 새 프로필 이미지는 없는 경우: 기존 이미지 경로 이동, 새 URL 할당
    String newProfileUrl = null;
    if (profileImage == null
        && requestDto.getNickname() != null
        && !requestDto.getNickname().equals(beforeNickname)) {
      newProfileUrl = imageService.changeProfilePath(beforeNickname, requestDto.getNickname());
    }

    // 3. 유저 정보 업데이트 (가장 우선: 새 업로드 > 경로 이동 > 기존값)
    user.updateProfile(
        requestDto.getEmail(),
        requestDto.getContact(),
        requestDto.getNickname(),
        requestDto.getAddress(),
        profileImageUrl != null ? profileImageUrl :
            (newProfileUrl != null ? newProfileUrl : user.getProfileImage())
    );

    // 4. 이메일 변경 시 refreshToken 무효화
    if (requestDto.getEmail() != null && !requestDto.getEmail().equals(beforeEmail)) {
      String redisKeyHeader = JwtConstants.REFRESH_TOKEN_HEADER + ":";
      refreshTokenService.deleteToken(redisKeyHeader + beforeEmail);
    }

    // 5. 응답 반환
    return UserConverter.toUpdateProfileDto(user);
  }

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
