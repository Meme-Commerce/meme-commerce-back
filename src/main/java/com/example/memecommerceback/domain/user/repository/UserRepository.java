package com.example.memecommerceback.domain.user.repository;


import com.example.memecommerceback.domain.user.entity.Users;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, UUID> {

  /**
 * 주어진 이메일 주소로 사용자를 조회합니다.
 *
 * @param email 조회할 사용자의 이메일 주소
 * @return 해당 이메일과 일치하는 사용자가 존재하면 Optional에 담아 반환하며, 없으면 빈 Optional을 반환합니다.
 */
Optional<Users> findByEmail(String email);
}

