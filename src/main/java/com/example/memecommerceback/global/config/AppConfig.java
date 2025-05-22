package com.example.memecommerceback.global.config;

import com.example.memecommerceback.global.jwt.cookie.CookieProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CookieProperties.class)
public class AppConfig {

}