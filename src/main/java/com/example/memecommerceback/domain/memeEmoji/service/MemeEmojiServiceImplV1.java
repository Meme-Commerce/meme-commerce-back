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

}
