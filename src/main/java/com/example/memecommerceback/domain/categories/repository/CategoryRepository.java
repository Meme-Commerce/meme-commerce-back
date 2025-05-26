package com.example.memecommerceback.domain.categories.repository;

import com.example.memecommerceback.domain.categories.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  boolean existsByNameIn(List<String> nameList);
  boolean existsByNameAndIdNot(String name, Long categoryId);
}

