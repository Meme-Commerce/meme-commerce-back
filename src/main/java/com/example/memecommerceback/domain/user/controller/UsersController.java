package com.example.memecommerceback.domain.user.controller;

import com.example.memecommerceback.domain.user.service.UsersServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UsersController {

  private final UsersServiceV1 usersServiceV1;
}
