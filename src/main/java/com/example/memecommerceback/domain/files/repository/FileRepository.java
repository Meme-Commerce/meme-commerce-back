package com.example.memecommerceback.domain.files.repository;

import java.util.UUID;
import com.example.memecommerceback.domain.files.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, UUID> {

}
