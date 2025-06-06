package com.example.memecommerceback.global.redis.utils;
import com.example.memecommerceback.global.redis.RedisKeyConstants;
import java.util.UUID;

public class RedisKeyUtils {
  public static String getProductStockKey(UUID productId){
    return RedisKeyConstants.PRODUCT
        +productId+":"+RedisKeyConstants.STOCK;
  }

  public static String getPopularProductAllKey(){
    return RedisKeyConstants.POPULAR_PRODUCTS_KEY + RedisKeyConstants.PRODUCT_GENDER_ALL;
  }

  public static String getOrderKey(UUID orderId) {
    return RedisKeyConstants.ORDER + orderId;
  }

  public static String getRecentlyViewedUserKey(UUID userId){
    return RedisKeyConstants.RECENT_USER + ":" + userId + ":product";
  }
}
