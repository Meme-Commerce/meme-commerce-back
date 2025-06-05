package com.example.memecommerceback.global.redis;

public class RedisKeyConstants {
  public static final String ORDER_PREFIX = "ORDER_";
  public static final String ORDER = "order:";
  public static final String POPULAR_PRODUCTS_KEY = "popular:products";
  public static final String PRODUCT_GENDER_ALL = ":all:all";
  public static final String PRODUCT = "product:";
  public static final String STOCK = "stock:";

  public static final String LUA_SCRIPT = """
            local key = KEYS[1]
            local requested = tonumber(ARGV[1])
            local currentStock = tonumber(redis.call('GET', key))
            if currentStock == nil then return -1 end
            if currentStock < requested then return 0 end
            redis.call('DECRBY', key, requested)
            return 1
        """;
  public static final String RECENT_USER = "recent:users";
}
