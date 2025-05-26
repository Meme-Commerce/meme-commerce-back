package com.example.memecommerceback.domain.hashtags.repository;

import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

}
