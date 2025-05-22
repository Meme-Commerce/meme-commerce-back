package com.example.memecommerceback.domain.userOAuthProvider.entity;

import com.example.memecommerceback.domain.user.entity.OAuthProvider;
import com.example.memecommerceback.domain.user.entity.Users;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_oauth_provider")
public class UserOAuthProvider extends CommonEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @JoinColumn(name = "users_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Users user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OAuthProvider provider;

  @Column(nullable = false, unique = true)
  private String providerId;

  @PrePersist
  public void generateId() {
    if (id == null) {
      this.id = UUID.randomUUID();
    }
  }
}
