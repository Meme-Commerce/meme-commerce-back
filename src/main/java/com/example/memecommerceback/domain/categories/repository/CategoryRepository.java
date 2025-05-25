package com.example.memecommerceback.domain.categories.repository;


import com.example.memecommerceback.domain.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

