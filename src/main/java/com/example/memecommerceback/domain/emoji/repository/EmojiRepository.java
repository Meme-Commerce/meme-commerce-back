package com.example.memecommerceback.domain.emoji.repository;


import com.example.memecommerceback.domain.emoji.entity.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

}

