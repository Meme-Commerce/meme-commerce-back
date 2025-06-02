package com.example.memecommerceback.domain.meme.repository;


import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.meme.entity.MemeStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemeRepository extends JpaRepository<Meme, Long> {
  List<Meme> findAllByStatus(MemeStatus status);

  Optional<Meme> findByIdAndStatus(Long memeId, MemeStatus status);

  Optional<Meme> findByIdAndRegisteredNickname(Long memeId, String registeredNickname);

  Page<Meme> findAllByYearAndQuarter(Pageable pageable, int year, int quarter);

  Page<Meme> findAllByYearAndQuarterAndStatus(
      Pageable pageable, int year, int quarter, MemeStatus status);
}

