package com.example.memecommerceback.domain.memeEmoji.service;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.emoji.service.EmojiServiceV1;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.meme.service.MemeServiceV1;
import com.example.memecommerceback.domain.memeEmoji.converter.MemeEmojiConverter;
import com.example.memecommerceback.domain.memeEmoji.dto.MemeEmojiResponseDto;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmoji;
import com.example.memecommerceback.domain.memeEmoji.entity.MemeEmojiStatus;
import com.example.memecommerceback.domain.memeEmoji.exception.MemeEmojiCustomException;
import com.example.memecommerceback.domain.memeEmoji.exception.MemeEmojiExceptionCode;
import com.example.memecommerceback.domain.memeEmoji.repository.MemeEmojiRepository;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.service.ProfanityFilterService;
import lombok.RequiredArgsConstructor;
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
      Long memeId, Long emojiId, String message, User requestUser){
    // 1. 해당 밈, 이모지가 존재하는지?
    Meme meme = memeService.findById(memeId);
    Emoji emoji = emojiService.findById(emojiId);

    // 2. 욕설 검증
    profanityFilterService.validateNoProfanity(message);

    // 3. 엔티티 작성 및 반환
    MemeEmoji memeEmoji = MemeEmojiConverter.toEntity(
        meme, emoji, message, requestUser.getNickname());
    memeEmojiRepository.save(memeEmoji);
    return MemeEmojiConverter.toCreateOneDto(memeEmoji, memeId, emojiId);
  }

  @Override
  @Transactional
  public MemeEmojiResponseDto.UpdateOneDto updateOneStatusByAdmin(
      Long memeEmojiId, boolean isApproved, String reason, User admin) {
    MemeEmoji memeEmoji
        = memeEmojiRepository.findByIdAndStatus(memeEmojiId, MemeEmojiStatus.PENDING)
        .orElseThrow(() -> new MemeEmojiCustomException(MemeEmojiExceptionCode.NOT_FOUND));
    profanityFilterService.validateNoProfanity(reason);

    memeEmoji.updateStatus(isApproved);

    return MemeEmojiConverter.toUpdateOneDto(memeEmoji);
  }

  @Override
  @Transactional
  public MemeEmojiResponseDto.UpdateOneDto updateOne(
      Long memeEmojiId, String message, User loginUser){
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
    profanityFilterService.validateNoProfanity(message);
    // 요청 사항 변경 및 반환
    memeEmoji.update(message);
    return MemeEmojiConverter.toUpdateOneDto(memeEmoji);
  }
}
