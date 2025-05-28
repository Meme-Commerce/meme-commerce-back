package com.example.memecommerceback.domain.files.service;

import com.example.memecommerceback.domain.files.entity.File;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceV1 {

  List<File> uploadUserFileList(
      List<MultipartFile> multipartFileList, User owner);

  void deleteUserWithFiles(UUID ownerId);

  List<File> findAllByOwnerIdAndFileType(UUID ownerId);
}
