package com.example.memecommerceback.domain.files.repository;

import com.example.memecommerceback.domain.files.entity.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, UUID> {

  List<File> findAllByOwnerId(UUID ownerId);
}
