package com.example.memecommerceback.domain.meme.repository;


import com.example.memecommerceback.domain.meme.entity.Meme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemeRepository extends JpaRepository<Meme, Long> {
  List<Meme> findAllByIsApprovedTrue();
}

