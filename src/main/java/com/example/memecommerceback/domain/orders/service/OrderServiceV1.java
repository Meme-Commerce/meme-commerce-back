package com.example.memecommerceback.domain.orders.service;

import com.example.memecommerceback.domain.orders.dto.OrderRequestDto;
import com.example.memecommerceback.domain.orders.dto.OrderResponseDto;
import com.example.memecommerceback.domain.users.entity.User;

public interface OrderServiceV1 {

  OrderResponseDto.CreateOneDto createOne(
      OrderRequestDto.CreateOneDto requestDto, User purchaser);
}
