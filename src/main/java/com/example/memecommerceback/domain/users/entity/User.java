package com.example.memecommerceback.domain.users.entity;

import com.example.memecommerceback.domain.files.entity.File;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends CommonEntity {

  // fields
  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(length = 20, unique = true)
  private String nickname;

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

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SellerStatus sellerStatus = SellerStatus.NONE;

  // relation
  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserOAuthProvider> oauthProviderList = new ArrayList<>();

  // entity method
  public void updateProfile(
      String contact, String nickname, String address, String profileImage) {
    if (contact != null && !contact.equals(this.contact)) {
      this.contact = contact;
    }
    if (nickname != null && !nickname.equals(this.nickname)) {
      this.nickname = nickname;
    }
    if (address != null && !address.equals(this.address)) {
      this.address = address;
    }
    if (profileImage != null && !profileImage.equals(this.profileImage)) {
      this.profileImage = profileImage;
    }
  }

  public void updateRole(UserRole role) {
    this.role = role;
  }

  public void addOAuthProvider(UserOAuthProvider oAuthProvider) {
    this.oauthProviderList.add(oAuthProvider);
  }

  public void updateNickname(String nickname){
    this.nickname = nickname;
  }
  public void updateProfileImage(String profileImage){
    this.profileImage = profileImage;
  }

  public void updateSellerStatus(SellerStatus sellerStatus){
    this.sellerStatus = sellerStatus;
  }
}
