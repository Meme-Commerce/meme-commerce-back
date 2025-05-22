package com.example.memecommerceback.domain.images.repository;


import com.example.memecommerceback.domain.images.entity.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {

}
