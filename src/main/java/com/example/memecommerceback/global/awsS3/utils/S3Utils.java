package com.example.memecommerceback.global.awsS3.utils;

import java.util.UUID;

public class S3Utils {

  public static final String USER_PREFIX = "users/";
  public static final String EMOJI_PREFIX = "/emojis/";
  public static final String PROFILE_PREFIX = "/profile/";
  public static final String PRODUCT_PREFIX = "/products/";
  public static final String CERTIFICATE_PREFIX = "/certificates";
  public static final String S3_URL
      = "https://meme-commerce-bucket.s3.ap-northeast-2.amazonaws.com/";

  public static String getS3UserProfilePrefix(String nickname){
    return USER_PREFIX + nickname + PROFILE_PREFIX;
  }

  public static String getS3UserProductPrefix(String nickname, UUID productId){
    return USER_PREFIX + nickname + PRODUCT_PREFIX + productId + "/";
  }

  public static String getS3UserEmojiPrefix(String nickname, UUID productId){
    return USER_PREFIX + nickname + EMOJI_PREFIX + productId + "/";
  }
}
