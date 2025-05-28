package com.example.memecommerceback.domain.files.service;

import com.example.memecommerceback.domain.files.converter.FileConverter;
import com.example.memecommerceback.domain.files.entity.File;
import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.files.exception.FileExceptionCode;
import com.example.memecommerceback.domain.files.repository.FileRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.awsS3.dto.S3FileResponseDto;
import com.example.memecommerceback.global.awsS3.service.S3Service;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImplV1 implements FileServiceV1 {

  private final S3Service s3Service;
  private final FileRepository fileRepository;

  @Override
  @Transactional
  public List<File> uploadUserFileList(
      List<MultipartFile> multipartFileList, User owner) {
    if(multipartFileList == null){
      throw new FileCustomException(FileExceptionCode.EMPTY_FILE_LIST);
    }
    for(MultipartFile multipartFile : multipartFileList){
      if( multipartFile == null || multipartFile.isEmpty()){
        throw new FileCustomException(FileExceptionCode.EMPTY_FILE);
      }
    }
    if (multipartFileList.size() < 4 || multipartFileList.size() > 10) {
      throw new FileCustomException(FileExceptionCode.COUNT_OUT_OF_RANGE);
    }
    List<S3FileResponseDto> s3ResponseDtoList
        = s3Service.uploadCertificateFileList(
        multipartFileList, owner.getNickname());
    List<File> fileList = FileConverter.toEntityList(s3ResponseDtoList, owner);
    fileRepository.saveAll(fileList);
    return fileList;
  }

  @Override
  @Transactional
  public void deleteUserWithFiles(UUID ownerId) {
    List<File> fileList = fileRepository.findAllByOwnerId(ownerId);
    for(File file : fileList){
      s3Service.deleteS3Object(file.getUrl());
    }
    fileRepository.deleteAll(fileList);
  }
}
