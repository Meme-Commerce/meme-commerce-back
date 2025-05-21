package com.example.memecommerceback.global.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

import com.example.memecommerceback.global.config.WebSecurityConfig;

public class WebSecurityConfigTests {

    private final ApplicationContextRunner contextRunner =
        new ApplicationContextRunner()
            .withUserConfiguration(WebSecurityConfig.class);

    @Test
    void whenContextLoads_thenPasswordEncoderBeanExistsAndIsBCrypt() {
        contextRunner.run(context -> {
            PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
            assertNotNull(encoder, "PasswordEncoder bean should not be null");
            assertTrue(encoder instanceof BCryptPasswordEncoder,
                "PasswordEncoder should be a BCryptPasswordEncoder");
        });
    }

    @Test
    void whenContextLoads_thenAuthenticationManagerBeanExists() {
        contextRunner.run(context -> {
            AuthenticationManager authManager =
                context.getBean("authenticationManager", AuthenticationManager.class);
            assertNotNull(authManager, "AuthenticationManager bean should be present");
        });
    }

    @Test
    void whenContextLoads_thenSecurityFilterChainBeanExists() {
        contextRunner.run(context -> {
            SecurityFilterChain filterChain =
                context.getBean(SecurityFilterChain.class);
            assertNotNull(filterChain, "SecurityFilterChain bean should be present");
        });
    }

    @Test
    void passwordEncoderShouldEncodeAndMatch() {
        contextRunner.run(context -> {
            PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
            String raw = "TestPassword123!";
            String encoded = encoder.encode(raw);
            assertNotNull(encoded, "Encoded password should not be null");
            assertTrue(encoder.matches(raw, encoded),
                "PasswordEncoder should correctly match raw and encoded passwords");
        });
    }
}