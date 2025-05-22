package com.example.memecommerceback.domain.users.entity;

import com.example.memecommerceback.domain.users.exception.UserCustomException;
import com.example.memecommerceback.domain.users.exception.UserExceptionCode;
import lombok.Getter;

@Getter
public enum UserRole {

  USER(Authority.USER),
  ADMIN(Authority.ADMIN),
  SELLER(Authority.SELLER),
  ;

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  public static class Authority {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String SELLER = "ROLE_SELLER";
  }

  public static UserRole fromAuthority(String authority) {
    for (UserRole role : UserRole.values()) {
      if (role.getAuthority().equals(authority)) {
        return role;
      }
    }
    throw new UserCustomException(UserExceptionCode.NOT_EXIST_AUTHORITY);
  }
}
