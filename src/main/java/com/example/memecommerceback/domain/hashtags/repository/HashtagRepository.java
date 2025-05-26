package com.example.memecommerceback.domain.hashtags.repository;

import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

  boolean existsByNameIn(List<String> nameList);

  boolean existsByNameAndIdNot(String name, Long id);
}
