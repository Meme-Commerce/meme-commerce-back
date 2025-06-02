package com.example.memecommerceback.domain.meme.service;

import com.example.memecommerceback.domain.meme.converter.MemeConverter;
import com.example.memecommerceback.domain.meme.dto.MemeRequestDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto;
import com.example.memecommerceback.domain.meme.dto.MemeResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.meme.entity.MemeStatus;
import com.example.memecommerceback.domain.meme.exception.MemeCustomException;
import com.example.memecommerceback.domain.meme.exception.MemeExceptionCode;
import com.example.memecommerceback.domain.meme.repository.MemeRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import com.example.memecommerceback.global.utils.DateUtils;
import com.example.memecommerceback.global.utils.RabinKarpUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemeServiceImplV1 implements MemeServiceV1 {

  private final ProfanityFilterService profanityFilterService;

  private final MemeRepository memeRepository;

  @Override
  @Transactional
  public MemeResponseDto.CreateDto create(
      MemeRequestDto.CreateDto requestDto, User user) {

    // 1. 최대 밈 생성 갯수 10개 제한
    if (requestDto.getCreateOneDtoList().size() > 10) {
      throw new MemeCustomException(MemeExceptionCode.MAX_CREATE_MEME_LIMIT_EXCEEDED);
    }

    // 2. 이름/설명 리스트 준비
    List<String> nameList = new ArrayList<>();
    List<String>
        descriptionList = new ArrayList<>();
    List<MemeRequestDto.CreateOneDto> createOneDtoList = requestDto.getCreateOneDtoList();

    // 3. 기존(승인된) 밈 목록 조회
    List<Meme> savedMemeList = memeRepository.findAllByStatus(MemeStatus.APPROVED);

    // 4. 신규 요청 각 밈에 대해 (1) 기존 밈과, (2) 자기들끼리 유사도 체크
    // 4-1. 기존(저장된) 밈과 유사도 검사
    for (MemeRequestDto.CreateOneDto requestOneDto : createOneDtoList) {
      String name = requestOneDto.getName();
      String description = requestOneDto.getDescription();

      for (Meme meme : savedMemeList) {
        double nameSimilarity = RabinKarpUtils.slidingWindowSimilarity(
            name, meme.getName(), RabinKarpUtils.WINDOW_SIZE);
        double descriptionSimilarity = RabinKarpUtils.slidingWindowSimilarity(
            description, meme.getDescription(), RabinKarpUtils.WINDOW_SIZE);

        if (nameSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
          throw new MemeCustomException(
              MemeExceptionCode.SIMILAR_NAME,
              "이름이 기존 밈 '" + meme.getName() + "'과 너무 유사합니다. (" + nameSimilarity + "%)");
        }
        if (descriptionSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
          throw new MemeCustomException(
              MemeExceptionCode.SIMILAR_DESCRIPTION,
              "설명이 기존 밈 '" + meme.getDescription() + "'과 너무 유사합니다. (" + descriptionSimilarity + "%)");
        }
      }
      nameList.add(name);
      descriptionList.add(description);
    }

    // 4-2. 신규 요청 밈들끼리도 유사도 검사 (중복 체크 포함)
    for (int i = 0; i < createOneDtoList.size(); i++) {
      String nameA = createOneDtoList.get(i).getName();
      String descA = createOneDtoList.get(i).getDescription();
      for (int j = i + 1; j < createOneDtoList.size(); j++) {
        String nameB = createOneDtoList.get(j).getName();
        String descB = createOneDtoList.get(j).getDescription();

        double nameSimilarity = RabinKarpUtils.slidingWindowSimilarity(
            nameA, nameB, RabinKarpUtils.WINDOW_SIZE);
        if (nameSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
          throw new MemeCustomException(
              MemeExceptionCode.SIMILAR_NAME,
              String.format("동일 요청 내 밈 이름 '%s'와 '%s'가 너무 유사합니다. (%.2f%%)", nameA, nameB, nameSimilarity));
        }

        double descSimilarity = RabinKarpUtils.slidingWindowSimilarity(
            descA, descB, RabinKarpUtils.WINDOW_SIZE);
        if (descSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
          throw new MemeCustomException(
              MemeExceptionCode.SIMILAR_DESCRIPTION,
              String.format("동일 요청 내 밈 설명이 너무 유사합니다. (%.2f%%)", descSimilarity));
        }
      }
    }

    // 5. 욕설 필터
    profanityFilterService.validateListNoProfanity(nameList);
    profanityFilterService.validateListNoProfanity(descriptionList);

    // 6. 저장 및 반환
    String registeredNickname = user.getNickname();
    int currentYear = LocalDate.now().getYear();
    int currentQuarter = DateUtils.getCurrentQuarter();
    List<Meme> memeList
        = MemeConverter.toEntityList(
        requestDto, registeredNickname, currentYear, currentQuarter);
    memeRepository.saveAll(memeList);
    return MemeConverter.toCreateDto(memeList, registeredNickname);
  }

  @Override
  @Transactional
  public MemeResponseDto.UpdateOneStatusDto updateOneStatusByAdmin(
      Long memeId, boolean isApproved, String notificationMessage, User admin) {
    Meme meme = memeRepository.findByIdAndStatus(memeId, MemeStatus.PENDING).orElseThrow(
        ()-> new MemeCustomException(MemeExceptionCode.NOT_FOUND));
    meme.updateStatus(isApproved);
    // notificationService.sendRejectReasonForUser
    return MemeConverter.toUpdateOneStatusDto(meme);
  }

  @Override
  @Transactional
  public MemeResponseDto.UpdateOneDto updateOne(
      Long memeId, MemeRequestDto.UpdateOneDto requestDto, User user) {
    // TODO : nickname과 memeId 복합 인덱스 구현
    Meme meme
        = memeRepository.findByIdAndRegisteredNickname(memeId, user.getNickname())
        .orElseThrow(() -> new MemeCustomException(MemeExceptionCode.NOT_FOUND));

    // 2. 검수 중인 밈만 수정 가능
    if(!meme.getStatus().equals(MemeStatus.PENDING)){
      throw new MemeCustomException(MemeExceptionCode.CANNOT_MODIFY_STATUS);
    }

    // 3. 욕설 검증
    profanityFilterService.validateNoProfanity(requestDto.getName());
    profanityFilterService.validateNoProfanity(requestDto.getDescription());

    // 4. 유사도 검증
    List<Meme> savedMemeList = memeRepository.findAllByStatus(MemeStatus.APPROVED);
    for(Meme savedMeme : savedMemeList){
      String savedMemeName = savedMeme.getName();
      String savedMemeDescription = savedMeme.getDescription();
      String requestedMemeName = requestDto.getName();
      String requestedMemeDescription = requestDto.getDescription();

      // 이름 유사도 검사 (null 체크 추가)
      if (savedMemeName != null && requestedMemeName != null) {
        double nameSimilarity = RabinKarpUtils.slidingWindowSimilarity(
            savedMemeName, requestedMemeName, RabinKarpUtils.WINDOW_SIZE);
        if (nameSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
          throw new MemeCustomException(
              MemeExceptionCode.SIMILAR_NAME,
              String.format("기존 밈 이름 '%s'와(과) 수정하려는 이름 '%s'가 너무 유사합니다. (%.2f%%)",
                  savedMemeName, requestedMemeName, nameSimilarity));
        }
      }

      // 설명 유사도 검사 (null 체크 추가) -> null이면 변경하지 않음.
      if (savedMemeDescription != null && requestedMemeDescription != null) {
        double descSimilarity = RabinKarpUtils.slidingWindowSimilarity(
            savedMemeDescription, requestedMemeDescription, RabinKarpUtils.WINDOW_SIZE);
        if (descSimilarity >= RabinKarpUtils.SIMILARITY_THRESHOLD) {
          throw new MemeCustomException(
              MemeExceptionCode.SIMILAR_DESCRIPTION,
              String.format("기존 밈 설명이 '%s'와(과) 수정하려는 설명 '%s'가 너무 유사합니다. (%.2f%%)",
                  savedMemeDescription, requestedMemeDescription, descSimilarity));
        }
      }
    }

    meme.update(requestDto.getName(), requestDto.getDescription());
    return MemeConverter.toUpdateOneDto(meme);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MemeResponseDto.ReadOneDto> readPageByAdmin(
      int page, int size, int year, int quarter){
    DateUtils.validateYearAndQuarter(year, quarter);
    Pageable pageable = PageRequest.of(page, size);
    Page<Meme> memePage
        = memeRepository.findAllByYearAndQuarter(pageable, year, quarter);
    return MemeConverter.toReadPage(memePage);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MemeResponseDto.ReadSummaryOneDto> readSummaryPage(
      int page, int size, int year, int quarter) {
    DateUtils.validateYearAndQuarter(year, quarter);
    Pageable pageable = PageRequest.of(page, size);
    Page<Meme> memePage
        = memeRepository.findAllByYearAndQuarterAndStatus(
        pageable, year, quarter, MemeStatus.APPROVED);
    return MemeConverter.toSummaryReadPage(memePage);
  }

  @Override
  @Transactional
  public void deleteMany(
      List<Long> deletedMemeIdList, String deletedReason, User admin) {
    List<Meme> memeList = memeRepository.findAllById(deletedMemeIdList);
    memeRepository.deleteAllById(deletedMemeIdList);
    // TODO 등록자에게 해당 밈을 삭제 하였다는 메세지 전달
  }

  @Override
  @Transactional(readOnly = true)
  public Meme findById(Long memeId){
    return memeRepository.findById(memeId).orElseThrow(
        ()-> new MemeCustomException(MemeExceptionCode.NOT_FOUND));
  }
}