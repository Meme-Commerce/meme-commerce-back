package com.example.memecommerceback.global.redis.service;

import com.example.memecommerceback.global.redis.RedisKeyConstants;
import com.example.memecommerceback.global.redis.repository.OrderNumberRepository;
import com.example.memecommerceback.global.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderNumberServiceImplV1 implements OrderNumberServiceV1{

  private final OrderNumberRepository orderNumberRepository;

  @Override
  @Transactional(readOnly = true)
  public String getTodayOrderNumberByToday(){
    String todayIsoDate = DateUtils.toTodayBasicIsoDateFormat();
    String todayOrderSequence
        = orderNumberRepository.getTodayOrderNumber(todayIsoDate);
    // order:today_00001
    return RedisKeyConstants.ORDER_PREFIX +todayIsoDate +"_"+todayOrderSequence;
  }

  @Override
  @Transactional
  public String setTodayOrderNumber(String todayIsoDate){
    return orderNumberRepository.save(todayIsoDate);
  }
}
