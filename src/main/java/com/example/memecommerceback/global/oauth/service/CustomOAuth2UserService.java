package com.example.memecommerceback.global.oauth.service;

import com.example.memecommerceback.domain.user.converter.UsersConverter;
import com.example.memecommerceback.domain.user.entity.OAuthProvider;
import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.domain.user.service.UsersServiceV1;
import com.example.memecommerceback.domain.userOAuthProvider.converter.UserOAuthProviderConverter;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.OAuth2CustomException;
import com.example.memecommerceback.global.oauth.constant.OAuthConstants;
import com.example.memecommerceback.global.oauth.entity.CustomOAuth2User;
import com.example.memecommerceback.global.utils.DateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CustomOAuth2UserService logs")
public class CustomOAuth2UserService extends DefaultOAuth2UserService
    implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UsersServiceV1 usersService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(request);
    String provider = request.getClientRegistration().getRegistrationId();

    switch (provider) {
      case OAuthConstants.NAVER_PROVIDER -> {
        return loadNaverUser(provider, oAuth2User);
      }
      case OAuthConstants.KAKAO_PROVIDER -> {
        return loadKakaoUser(provider, oAuth2User);
      }
      default -> throw new OAuth2CustomException(
          GlobalExceptionCode.NOT_FOUND_PROVIDER);
    }
  }

  private OAuth2User loadNaverUser(String provider, OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();

    log.info("네이버에서 받은 유저 정보: {}", attributes);
    Map<String, Object> response
        = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      throw new OAuth2CustomException(
          GlobalExceptionCode.NOT_FOUND_RESPONSE);
    }

    String id = (String) response.get(OAuthConstants.ID);
    String email = (String) response.get(OAuthConstants.EMAIL);
    String name = (String) response.get(OAuthConstants.NAME);
    String gender = (String) response.get(OAuthConstants.GENDER);
    String birthYear = (String) response.get(OAuthConstants.BIRTH_YEAR);
    String birthDay = (String) response.get(OAuthConstants.BIRTH_DAY);
    String contact = (String) response.get(OAuthConstants.NAVER_MOBILE);

    LocalDate birthDate
        = DateUtils.parse(birthYear, birthDay, provider);
    Integer age = DateUtils.calculateAge(birthDate);
    validateField(id, GlobalExceptionCode.NOT_FOUND_RESPONSE_ID);
    validateField(email, GlobalExceptionCode.NOT_FOUND_RESPONSE_EMAIL);
    validateField(name, GlobalExceptionCode.NOT_FOUND_RESPONSE_NAME);
    validateField(gender, GlobalExceptionCode.NOT_FOUND_RESPONSE_GENDER);
    validateField(birthYear, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHYEAR);
    validateField(birthDay, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHDAY);
    validateField(contact, GlobalExceptionCode.NOT_FOUND_RESPONSE_CONTACT);

    String oauthId = provider + "_" + id;

    OAuthProvider oAuthProvider
        = OAuthProvider.fromCode(provider.charAt(0));
    // 유저 조회 또는 생성
    return validateUser(
        oAuthProvider, response, contact, email,
        oauthId, name, gender, birthDate, age);
  }

  private OAuth2User loadKakaoUser(String provider, OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();
    log.info("카카오에서 받은 유저 정보: {}", attributes);

    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

    if (kakaoAccount == null) {
      throw new OAuth2CustomException(GlobalExceptionCode.NOT_FOUND_RESPONSE);
    }

    String id = String.valueOf(attributes.get(OAuthConstants.ID));
    String email = (String) kakaoAccount.get(OAuthConstants.EMAIL);
    String name = (String) kakaoAccount.get(OAuthConstants.NAME);
    String gender = (String) kakaoAccount.get(OAuthConstants.GENDER);
    String birthYear = (String) kakaoAccount.get(OAuthConstants.BIRTH_YEAR);
    String birthDay = (String) kakaoAccount.get(OAuthConstants.BIRTH_DAY);
    String contact = (String) kakaoAccount.get(OAuthConstants.KAKAO_MOBILE);

    // 2000-0730
    LocalDate birthDate = DateUtils.parse(birthYear, birthDay, provider);
    Integer age = DateUtils.calculateAge(birthDate);

    validateField(id, GlobalExceptionCode.NOT_FOUND_RESPONSE_ID);
    validateField(email, GlobalExceptionCode.NOT_FOUND_RESPONSE_EMAIL);
    validateField(name, GlobalExceptionCode.NOT_FOUND_RESPONSE_NAME);
    validateField(gender, GlobalExceptionCode.NOT_FOUND_RESPONSE_GENDER);
    validateField(birthYear, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHYEAR);
    validateField(birthDay, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHDAY);
    validateField(contact, GlobalExceptionCode.NOT_FOUND_RESPONSE_CONTACT);

    String oauthId = provider + "_" + id;

    OAuthProvider oAuthProvider
        = OAuthProvider.fromCode(provider.charAt(0));

    // contact +82 10-7***-**** 해결해야함
    String removePrefixContact =
        "0" + contact.substring(4, 16);
    return validateUser(
        oAuthProvider, attributes, removePrefixContact,
        email, oauthId, name, gender, birthDate, age);
  }

  private OAuth2User validateUser(
      OAuthProvider oAuthProvider, Map<String, Object> attributes,
      String contact, String email, String oauthId, String name,
      String gender, LocalDate birthDate, Integer age) {

    List<Users> duplicatedContactUserList
        = usersService.findByContactFetchOAuth(contact);

    for (Users user : duplicatedContactUserList) {
      boolean exists = user.getOauthProviderList().stream()
          .anyMatch(p -> p.getProvider().equals(oAuthProvider));
      if (exists) {
        return new CustomOAuth2User(user, attributes);
      }
    }

    // 연동된 provider가 없을 때 → 정책적으로 처리
    if (duplicatedContactUserList.isEmpty()) {
      // 신규 유저 생성
      Users newUser
          = UsersConverter.toEntity(
          email, oauthId, oAuthProvider,
          name, gender, contact, birthDate, age);
      usersService.save(newUser);
      return new CustomOAuth2User(newUser, attributes);
    } else {
      // 기존 유저에 새로운 provider 연동
      Users existingUser = duplicatedContactUserList.get(0);
      existingUser.addOAuthProvider(
          UserOAuthProviderConverter.toEntity(existingUser, oauthId, oAuthProvider)
      );
      usersService.save(existingUser);
      return new CustomOAuth2User(existingUser, attributes);
    }
  }

  private void validateField(String value, GlobalExceptionCode exceptionCode) {
    if (value == null || value.isBlank()) {
      throw new OAuth2CustomException(exceptionCode);
    }
  }
}
