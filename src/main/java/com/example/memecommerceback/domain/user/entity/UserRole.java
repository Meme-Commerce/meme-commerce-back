package com.example.memecommerceback.domain.user.entity;

import com.example.memecommerceback.domain.user.exception.UsersCustomException;
import com.example.memecommerceback.domain.user.exception.UsersExceptionCode;
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
    throw new UsersCustomException(UsersExceptionCode.NOT_EXIST_AUTHORITY);
  }
}
