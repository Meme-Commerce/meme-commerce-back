package com.example.memecommerceback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MemeCommerceBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(MemeCommerceBackApplication.class, args);
  }

}
