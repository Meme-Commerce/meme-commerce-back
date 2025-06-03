package com.example.memecommerceback.domain.memeEmoji.service;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.emoji.service.EmojiServiceV1;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.meme.service.MemeServiceV1;
import com.example.memecommerceback.domain.memeEmoji.converter.MemeEmojiConverter;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto.ReadOneDto;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmojiStatus;
import com.example.memecommerceback.domain.memeEmoji.exception.MemeEmojiCustomException;
import com.example.memecommerceback.domain.memeEmoji.exception.MemeEmojiExceptionCode;
import com.example.memecommerceback.domain.memeEmoji.repository.MemeEmojiRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemeEmojiServiceImplV1 implements MemeEmojiServiceV1 {

  private final MemeServiceV1 memeService;
  private final EmojiServiceV1 emojiService;
  private final ProfanityFilterService profanityFilterService;

  private final MemeEmojiRepository memeEmojiRepository;

  // TODO : 밈모지 생성, 상태 수정(승인/거절)은 알림을 구현해야함.

  @Override
  @Transactional
  public MemeEmojiResponseDto.CreateOneDto createOne(
      Long memeId, Long emojiId, String name, String message, User requestUser){
    // 1. 해당 밈, 이모지가 존재하는지?
    Meme meme = memeService.findById(memeId);
    Emoji emoji = emojiService.findById(emojiId);

    // 2. 욕설 검증
    profanityFilterService.validateNoProfanity(name);
    profanityFilterService.validateNoProfanity(message);

    // 3. 엔티티 작성 및 반환
    MemeEmoji memeEmoji = MemeEmojiConverter.toEntity(
        meme, emoji, name, message, requestUser.getNickname());
    memeEmojiRepository.save(memeEmoji);
    return MemeEmojiConverter.toCreateOneDto(memeEmoji, memeId, emojiId);
  }

  @Override
  @Transactional
  public MemeEmojiResponseDto.UpdateOneDto updateOneStatusByAdmin(
      Long memeEmojiId, boolean isApproved, String reason, User admin) {
    // 해당 밈모지 찾기
    MemeEmoji memeEmoji = findById(memeEmojiId);

    // 욕설 검증
    profanityFilterService.validateNoProfanity(reason);

    // 상태 변경 검증
    MemeEmojiStatus beforeStatus = memeEmoji.getStatus();
    switch (beforeStatus) {
      case PENDING -> memeEmoji.updateStatus(isApproved);
      case APPROVED -> {
        if(isApproved)
          throw new MemeEmojiCustomException(MemeEmojiExceptionCode.REQUEST_SAME_STATUS);
        memeEmoji.updateStatus(false);
      }
      case REJECTED -> {
        if(!isApproved)
          throw new MemeEmojiCustomException(MemeEmojiExceptionCode.REQUEST_SAME_STATUS);
        memeEmoji.updateStatus(true);
      }
      default -> throw new MemeEmojiCustomException(MemeEmojiExceptionCode.NOT_EXIST_STATUS);
    }

    // 반환
    return MemeEmojiConverter.toUpdateOneDto(memeEmoji);
  }

  @Override
  @Transactional
  public MemeEmojiResponseDto.UpdateOneDto updateOne(
      Long memeEmojiId, String name, String message, User loginUser){
    // 밈모지 찾기
    MemeEmoji memeEmoji = memeEmojiRepository.findById(memeEmojiId).orElseThrow(
        ()-> new MemeEmojiCustomException(MemeEmojiExceptionCode.NOT_FOUND));
    // 밈모지의 상태가 PENDING 상태인지?
    if(memeEmoji.getStatus().equals(MemeEmojiStatus.PENDING)){
      throw new MemeEmojiCustomException(MemeEmojiExceptionCode.ALREADY_COMPLETED_STATUS);
    }
    // 밈모지 주인이 맞는지 확인
    if(memeEmoji.getRequestUserNickname().equals(loginUser.getNickname())){
      throw new MemeEmojiCustomException(MemeEmojiExceptionCode.NOT_OWNER);
    }
    // 요청사항 욕설 검증
    profanityFilterService.validateNoProfanity(name);
    profanityFilterService.validateNoProfanity(message);
    // 요청 사항 변경 및 반환
    memeEmoji.update(message);
    return MemeEmojiConverter.toUpdateOneDto(memeEmoji);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReadOneDto> readPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<MemeEmoji> memeEmojiPage
        = memeEmojiRepository.findAllByStatus(pageable, MemeEmojiStatus.APPROVED);
    return MemeEmojiConverter.toReadPageDto(memeEmojiPage);
  }

  @Override
  @Transactional(readOnly = true)
  public MemeEmojiResponseDto.ReadOneDto readOne(Long memeEmojiId, User loginUser) {
    MemeEmoji memeEmoji = findById(memeEmojiId);
    if(!loginUser.getNickname().equals(memeEmoji.getRequestUserNickname())
        && !memeEmoji.getStatus().equals(MemeEmojiStatus.APPROVED)){
      throw new MemeEmojiCustomException(MemeEmojiExceptionCode.UNAUTHORIZED_READ);
    }
    return MemeEmojiConverter.toReadOneDto(memeEmoji);
  }

  @Override
  @Transactional
  public void deleteMany(
      List<Long> deletedMemeIdList, String deletedReason, User admin) {
    List<MemeEmoji> memeEmojiList
        = memeEmojiRepository.findAllById(deletedMemeIdList);
    memeEmojiRepository.deleteAllById(deletedMemeIdList);
  }

  @Override
  @Transactional(readOnly = true)
  public MemeEmoji findById(Long memeEmojiId){
    return memeEmojiRepository.findById(memeEmojiId).orElseThrow(
        ()-> new MemeEmojiCustomException(MemeEmojiExceptionCode.NOT_FOUND));
  }
}
