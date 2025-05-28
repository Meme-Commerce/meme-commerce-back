package com.example.memecommerceback.domain.files.service;

import com.example.memecommerceback.domain.files.entity.File;
import com.example.memecommerceback.domain.files.entity.FileType;
import com.example.memecommerceback.domain.users.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 관리 서비스 인터페이스
 * <p>
 * 사용자 파일의 업로드, 삭제, 조회 등의 기능을 제공합니다. 주로 판매자 인증서와 같은 사용자 관련 파일들을 관리합니다.
 * </p>
 *
 * @author Meme Commerce Team
 * @version 1.0
 * @since 1.0
 */
public interface FileServiceV1 {

  /**
   * 사용자의 파일 목록을 업로드합니다.
   * <p>
   * 다중 파일 업로드를 지원하며, 업로드된 파일들은 지정된 소유자(User)와 연관됩니다. 파일 업로드 시 S3 저장소에 저장되고, 파일 메타데이터는 데이터베이스에
   * 저장됩니다.
   * </p>
   *
   * @param multipartFileList 업로드할 파일 목록 (null이면 안됨)
   * @param owner             파일의 소유자 (null이면 안됨)
   * @return 업로드된 파일 엔티티 목록
   */
  List<File> uploadUserFileList(
      List<MultipartFile> multipartFileList, User owner);

  /**
   * 지정된 소유자의 모든 파일을 삭제합니다.
   * <p>
   * 소유자 ID에 해당하는 모든 파일을 S3 저장소와 데이터베이스에서 삭제합니다. 이 작업은 되돌릴 수 없으므로 주의해서 사용해야 합니다.
   * </p>
   *
   * @param ownerId 파일을 삭제할 소유자의 UUID (null이면 안됨)
   */
  void deleteUserWithFiles(UUID ownerId);

  /**
   * 소유자 ID로 특정 파일 타입의 파일 목록을 조회합니다.
   * <p>
   * 현재 구현에서는 판매자 인증서(SELLER_CERTIFICATE) 타입의 파일만 조회합니다. 향후 FileType 매개변수를 추가하여 다양한 파일 타입을 지원할
   * 예정입니다.
   * </p>
   *
   * @param ownerId  파일을 조회할 소유자의 UUID (null이면 안됨)
   * @param fileType : {@link FileType}
   * @return 조회된 파일 엔티티 목록 (빈 목록일 수 있음)
   */
  List<File> findAllByOwnerIdAndFileType(UUID ownerId, FileType fileType);

}