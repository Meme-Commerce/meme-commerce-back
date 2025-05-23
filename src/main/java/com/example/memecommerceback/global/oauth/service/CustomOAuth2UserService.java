package com.example.memecommerceback.global.oauth.service;

import com.example.memecommerceback.domain.userOAuthProvider.converter.UserOAuthProviderConverter;
import com.example.memecommerceback.domain.users.converter.UserConverter;
import com.example.memecommerceback.domain.users.entity.OAuthProvider;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.service.UserServiceV1;
import com.example.memecommerceback.global.exception.GlobalExceptionCode;
import com.example.memecommerceback.global.exception.OAuth2CustomException;
import com.example.memecommerceback.global.oauth.constant.OAuthConstants;
import com.example.memecommerceback.global.oauth.entity.CustomOAuth2User;
import com.example.memecommerceback.global.utils.ContactUtils;
import com.example.memecommerceback.global.utils.DateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  private final UserServiceV1 usersService;

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

    validateField(id, GlobalExceptionCode.NOT_FOUND_RESPONSE_ID);
    validateField(email, GlobalExceptionCode.NOT_FOUND_RESPONSE_EMAIL);
    validateField(name, GlobalExceptionCode.NOT_FOUND_RESPONSE_NAME);
    validateField(gender, GlobalExceptionCode.NOT_FOUND_RESPONSE_GENDER);
    validateField(birthYear, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHYEAR);
    validateField(birthDay, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHDAY);
    validateField(contact, GlobalExceptionCode.NOT_FOUND_RESPONSE_CONTACT);

    LocalDate birthDate
        = DateUtils.parse(birthYear, birthDay, provider);
    Integer age = DateUtils.calculateAge(birthDate);

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

    Map<String, Object> kakaoAccount
        = (Map<String, Object>) attributes.get("kakao_account");

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

    validateField(id, GlobalExceptionCode.NOT_FOUND_RESPONSE_ID);
    validateField(email, GlobalExceptionCode.NOT_FOUND_RESPONSE_EMAIL);
    validateField(name, GlobalExceptionCode.NOT_FOUND_RESPONSE_NAME);
    validateField(gender, GlobalExceptionCode.NOT_FOUND_RESPONSE_GENDER);
    validateField(birthYear, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHYEAR);
    validateField(birthDay, GlobalExceptionCode.NOT_FOUND_RESPONSE_BIRTHDAY);
    validateField(contact, GlobalExceptionCode.NOT_FOUND_RESPONSE_CONTACT);

    // 2000-0730, parsing 위치 변경
    LocalDate birthDate = DateUtils.parse(birthYear, birthDay, provider);
    Integer age = DateUtils.calculateAge(birthDate);

    String oauthId = provider + "_" + id;

    OAuthProvider oAuthProvider
        = OAuthProvider.fromCode(provider.charAt(0));

    String removePrefixContact = ContactUtils.normalizeContact(contact);

    return validateUser(
        oAuthProvider, attributes, removePrefixContact,
        email, oauthId, name, gender, birthDate, age);
  }

  private OAuth2User validateUser(
      OAuthProvider oAuthProvider, Map<String, Object> attributes,
      String contact, String email, String oauthId, String name,
      String gender, LocalDate birthDate, Integer age) {

    Optional<User> userOpt = usersService.findByContactFetchOAuth(contact);

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      boolean exists = user.getOauthProviderList().stream()
          .anyMatch(p -> p.getProvider().equals(oAuthProvider));
      if (exists) {
        // 이미 해당 provider 연동된 사용자
        return new CustomOAuth2User(user, attributes);
      } else {
        // 기존 유저에 새로운 provider 연동
        user.addOAuthProvider(
            UserOAuthProviderConverter.toEntity(user, oauthId, oAuthProvider)
        );
        usersService.save(user);
        return new CustomOAuth2User(user, attributes);
      }
    } else {
      // 연동된 유저가 없으면 → 신규 유저 생성
      User newUser = UserConverter.toEntity(
          email, oauthId, oAuthProvider, name, gender, contact, birthDate, age
      );
      usersService.save(newUser);
      return new CustomOAuth2User(newUser, attributes);
    }
  }

  private void validateField(String value, GlobalExceptionCode exceptionCode) {
    if (value == null || value.isBlank()) {
      throw new OAuth2CustomException(exceptionCode);
    }
  }
}
