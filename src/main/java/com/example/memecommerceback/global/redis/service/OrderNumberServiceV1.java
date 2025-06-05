package com.example.memecommerceback.global.redis.service;

public interface OrderNumberServiceV1 {

  String setTodayOrderNumber(String todayIsoDate);

  String getTodayOrderNumberByToday();
}
