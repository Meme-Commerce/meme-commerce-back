package com.example.memecommerceback.global.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WebSecurityConfigTests.TestEndpoints.class)
public class WebSecurityConfigTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads_andBeansArePresent() {
        // Verify WebSecurityConfig is loaded
        assertThat(applicationContext.getBean(WebSecurityConfig.class)).isNotNull();
        // Verify PasswordEncoder bean exists and works
        assertThat(passwordEncoder).isNotNull();
        String raw = "password123";
        String encoded = passwordEncoder.encode(raw);
        assertThat(passwordEncoder.matches(raw, encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrong", encoded)).isFalse();
    }

    @Test
    void whenGetPublicEndpoint_thenOkWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/public/ping"))
               .andExpect(status().isOk());
    }

    @Test
    void whenGetPrivateEndpoint_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/private/secret"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void whenGetLoginPage_thenOk() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk());
    }

    @RestController
    static class TestEndpoints {

        @GetMapping("/public/ping")
        public String publicPing() {
            return "pong";
        }

        @GetMapping("/private/secret")
        public String privateSecret() {
            return "secret";
        }
    }
}