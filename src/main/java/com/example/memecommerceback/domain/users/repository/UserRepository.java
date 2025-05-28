package com.example.memecommerceback.domain.users.repository;

import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.domain.users.entity.UserRole;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {
  /**
   * 이메일로 사용자를 검색합니다.
   *
   * @param email 검색할 이메일 주소
   * @return 검색된 사용자 정보 (Optional)
   */
  Optional<User> findByEmail(String email);

  /**
   * 연락처로 사용자를 검색하고 OAuth 제공자 정보를 함께 가져옵니다.
   * 이 메서드는 사용자와 관련된 OAuth 제공자 정보를 즉시 로드하는 fetch join을 사용합니다.
   *
   * @param contact 검색할 사용자의 연락처
   * @return 검색된 사용자 정보와 OAuth 제공자 정보 (Optional)
   */
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.oauthProviderList WHERE u.contact = :contact")
  Optional<User> findByContactFetchOAuth(@Param("contact") String contact);

  /**
   * 닉네임이 이미 존재하는지 확인합니다.
   *
   * @param nickname 확인할 닉네임
   * @return 닉네임이 존재하면 true, 존재하지 않으면 false
   */
  Boolean existsByNickname(String nickname);

  Optional<User> findByIdAndRole(UUID loginUserId, UserRole role);
}