package com.example.memecommerceback.domain.users.service;

import com.example.memecommerceback.domain.images.service.ImageServiceV1;
import com.example.memecommerceback.domain.users.converter.UserConverter;
import com.example.memecommerceback.domain.users.dto.UserRequestDto;
import com.example.memecommerceback.domain.users.dto.UserResponseDto;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.exception.UserCustomException;
import com.example.memecommerceback.domain.users.exception.UserExceptionCode;
import com.example.memecommerceback.domain.users.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserServiceV1 {

  private final ImageServiceV1 imagesService;
  private final UserRepository usersRepository;

  @Override
  @Transactional
  public void save(User user) {
    usersRepository.save(user);
  }

  @Override
  @Transactional
  public List<User> findByContactFetchOAuth(String contact) {
    return usersRepository.findByContactFetchOAuth(contact);
  }

  @Override
  @Transactional
  public UserResponseDto.UpdateProfileDto updateProfile(
      UserRequestDto.UpdateProfileDto requestDto,
      MultipartFile profileImage, User loginUser) {
    User user = findById(loginUser.getId());

    String profileImageUrl = getProfileImageUrl(profileImage, user);

    // 유저 정보 업데이트
    user.updateProfile(
        requestDto.getEmail() != null
            ? requestDto.getEmail() : user.getEmail(),
        requestDto.getContact() != null
            ? requestDto.getContact() : user.getContact(),
        requestDto.getNickname() != null
            ? requestDto.getNickname() : user.getNickname(),
        requestDto.getAddress() != null
            ? requestDto.getAddress() : user.getAddress(),
        profileImageUrl != null ? profileImageUrl : user.getProfileImage());

    return UserConverter.toUpdateProfileDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(UUID loginUserId){
    return usersRepository.findById(loginUserId).orElseThrow(
        ()-> new UserCustomException(UserExceptionCode.NOT_FOUND));
  }

  private String getProfileImageUrl(MultipartFile profileImage, User user) {
    if (profileImage == null) {
      return null; // 이미지가 없으면 null 반환
    }

    return imagesService.uploadAndRegisterUserProfileImage(profileImage, user);
  }
}
