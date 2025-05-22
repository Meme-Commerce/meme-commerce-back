package com.example.memecommerceback.domain.user.entity;

import com.example.memecommerceback.domain.userOAuthProvider.entity.UserOAuthProvider;
import com.example.memecommerceback.global.common.CommonEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "users")
public class Users extends CommonEntity {

  // fields
  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(length = 20, unique = true)
  private String nickname;

  @Column(name = "oauth_id", nullable = false, unique = true, length = 100)
  private String oauthId;

  @Column(name = "profile_image")
  private String profileImage;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Column(nullable = false)
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  private Gender gender;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String contact;

  @Column(nullable = false)
  private LocalDate birthDate;

  private String address;

  @Builder.Default
  private String companyName = null;

  // relation
  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserOAuthProvider> oauthProviderList = new ArrayList<>();

  // entity method
  @PrePersist
  public void generateId() {
    if (id == null) {
      this.id = UUID.randomUUID();
    }
  }

  public void updateProfile(
      String email, String contact,
      String nickname, String address, String profileImage) {
    this.email = email;
    this.contact = contact;
    this.nickname = nickname;
    this.address = address;
    this.profileImage = profileImage;
  }

  public void updateRole(UserRole role) {
    this.role = role;
  }

  public void addOAuthProvider(UserOAuthProvider oAuthProvider) {
    this.oauthProviderList.add(oAuthProvider);
  }
}
