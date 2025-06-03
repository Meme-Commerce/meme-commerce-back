package com.example.memecommerceback.domain.memeEmoji.entity;

import com.example.memecommerceback.domain.emoji.entity.Emoji;
import com.example.memecommerceback.domain.meme.entity.Meme;
import com.example.memecommerceback.domain.meme.entity.MemeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meme_emoji")
public class MemeEmoji {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "meme_emoji_seq_generator")
  @SequenceGenerator(
      name = "meme_emoji_seq_generator",
      sequenceName = "meme_emoji_seq",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private String requestUserNickname;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MemeEmojiStatus status = MemeEmojiStatus.PENDING;

  // 단방향 연관 관계 : MemeEmoji에서 meme, emoji가 삭제되더라도,
  // 상품화 했다면 Meme, Emoji의 삭제로 인해서 MemeEmoji가 삭제되면 안됨.
  @JoinColumn(name = "meme_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Meme meme;

  @JoinColumn(name = "emoji_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Emoji emoji;

  public void updateStatus(boolean isApproved){
    if(isApproved){
      this.status = MemeEmojiStatus.APPROVED;
    }else{
      this.status = MemeEmojiStatus.REJECTED;
    }
  }

  public void update(String message){
    this.message = message;
  }
}
