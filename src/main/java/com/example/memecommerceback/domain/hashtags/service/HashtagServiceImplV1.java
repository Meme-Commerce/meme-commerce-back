package com.example.memecommerceback.domain.hashtags.service;

import com.example.memecommerceback.domain.hashtags.converter.HashtagConverter;
import com.example.memecommerceback.domain.hashtags.dto.HashtagRequestDto;
import com.example.memecommerceback.domain.hashtags.dto.HashtagResponseDto;
import com.example.memecommerceback.domain.hashtags.entity.Hashtag;
import com.example.memecommerceback.domain.hashtags.exception.HashtagCustomException;
import com.example.memecommerceback.domain.hashtags.exception.HashtagExceptionCode;
import com.example.memecommerceback.domain.hashtags.repository.HashtagRepository;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImplV1 implements HashtagServiceV1 {

  private final ProfanityFilterService profanityFilterService;
  private final HashtagRepository hashtagRepository;

  @Override
  @Transactional
  public HashtagResponseDto.CreateDto create(
      HashtagRequestDto.CreateDto requestDto) {
    if (hashtagRepository.existsByNameIn(requestDto.getNameList())) {
      throw new HashtagCustomException(HashtagExceptionCode.ALREADY_EXIST_NAME);
    }
    profanityFilterService.validateListNoProfanity(requestDto.getNameList());

    List<Hashtag> hashtagList = HashtagConverter.toEntityList(requestDto);
    hashtagRepository.saveAll(hashtagList);

    return HashtagConverter.toCreateDto(hashtagList);
  }

  @Override
  @Transactional
  public HashtagResponseDto.UpdateOneDto updateOne(
      Long hashtagId, String name) {
    Hashtag hashtag = findById(hashtagId);
    if(hashtagRepository.existsByNameAndIdNot(name, hashtagId)){
      throw new HashtagCustomException(HashtagExceptionCode.ALREADY_EXIST_NAME);
    }
    profanityFilterService.validateNoProfanity(name);
    hashtag.update(name);
    return HashtagConverter.toUpdateOneDto(hashtag);
  }

  @Override
  @Transactional
  public void delete(HashtagRequestDto.DeleteDto requestDto) {
    List<Long> requestedIdList = requestDto.getHashtagIdList();
    List<Hashtag> hashtagList = hashtagRepository.findAllById(requestedIdList);

    Set<Long> foundIdList = hashtagList.stream()
        .map(Hashtag::getId)
        .collect(Collectors.toSet());

    Set<Long> notFoundIdList = requestedIdList.stream()
        .filter(id -> !foundIdList.contains(id))
        .collect(Collectors.toSet());

    if (!notFoundIdList.isEmpty()) {
      throw new HashtagCustomException(HashtagExceptionCode.NOT_FOUND,
          "요청하신 해시태그 아이디 " + notFoundIdList + "에 대한 해시태그 정보가 없습니다.");
    }

    hashtagRepository.deleteAllById(requestedIdList);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<HashtagResponseDto.ReadOneDto> readPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Hashtag> hashtagPage = hashtagRepository.findAll(pageable);
    return HashtagConverter.toReadPageDto(hashtagPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Hashtag findById(Long hashtagId) {
    return hashtagRepository.findById(hashtagId).orElseThrow(
        () -> new HashtagCustomException(HashtagExceptionCode.NOT_FOUND));
  }
}
