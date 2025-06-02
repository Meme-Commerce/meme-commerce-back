package com.example.memecommerceback.domain.memeEmoji.repository;


import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmojiStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemeEmojiRepository extends JpaRepository<MemeEmoji, Long> {
  Optional<MemeEmoji> findByIdAndStatus(Long memeEmojiId, MemeEmojiStatus status);
}

