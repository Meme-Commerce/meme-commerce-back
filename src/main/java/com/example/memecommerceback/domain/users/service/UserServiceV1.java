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

  UserResponseDto.UpdateProfileDto updateNickname(
      String requestedNickname, User loginUser);
}
