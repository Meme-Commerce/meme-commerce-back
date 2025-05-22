package com.example.memecommerceback.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  // Redis A (Token)
  @Value("${spring.data.redis.token.host}")
  private String tokenHost;

  @Value("${spring.data.redis.token.port}")
  private int tokenPort;

  // Redis B (Popular Product)
  @Value("${spring.data.redis.popular.host}")
  private String popularHost;

  @Value("${spring.data.redis.popular.port}")
  private int popularPort;

  // Redis C (Stock Lock)
  @Value("${spring.data.redis.stock.host}")
  private String stockHost;

  @Value("${spring.data.redis.stock.port}")
  private int stockPort;

  // Redis A
  @Bean
  public JedisConnectionFactory redisConnectionFactoryForToken() {
    RedisStandaloneConfiguration config
        = new RedisStandaloneConfiguration(tokenHost, tokenPort);
    return new JedisConnectionFactory(config);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplateForToken(
      @Qualifier("redisConnectionFactoryForToken")
      JedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  // Redis B
  @Bean
  public JedisConnectionFactory redisConnectionFactoryForPopularProduct() {
    RedisStandaloneConfiguration config
        = new RedisStandaloneConfiguration(popularHost, popularPort);
    return new JedisConnectionFactory(config);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplateForPopularProduct(
      @Qualifier("redisConnectionFactoryForPopularProduct")
      JedisConnectionFactory factory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }

  // Redis C
  @Bean
  public JedisConnectionFactory redisConnectionFactoryForStockLock() {
    RedisStandaloneConfiguration config
        = new RedisStandaloneConfiguration(stockHost, stockPort);
    return new JedisConnectionFactory(config);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplateForStockLock(
      @Qualifier("redisConnectionFactoryForStockLock")
      JedisConnectionFactory factory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }

  // (선택) Redis C 재사용: Order Number Sequence
  @Bean
  public RedisTemplate<String, String> redisTemplateForOrderNumberSequence(
      @Qualifier("redisConnectionFactoryForStockLock")
      JedisConnectionFactory factory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }
}
