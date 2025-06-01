package com.example.memecommerceback.domain.emoji.repository;


import com.example.memecommerceback.domain.emoji.entity.Emoji;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

  List<Emoji> findAllByProductId(UUID productId);
}

