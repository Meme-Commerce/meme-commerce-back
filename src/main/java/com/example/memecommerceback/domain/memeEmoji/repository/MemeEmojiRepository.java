package com.example.memecommerceback.domain.memeEmoji.repository;


import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmojiStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemeEmojiRepository extends JpaRepository<MemeEmoji, Long> {

  Page<MemeEmoji> findAllByStatus(Pageable pageable, MemeEmojiStatus status);
}

