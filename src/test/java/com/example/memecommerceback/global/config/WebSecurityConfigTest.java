package com.example.memecommerceback.global.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Public Endpoints")
    class PublicEndpoints {

        @Test
        @DisplayName("GET /actuator/health is public")
        void healthEndpointAccessible() throws Exception {
            mockMvc.perform(get("/actuator/health"))
                   .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /swagger-ui/index.html is public")
        void swaggerUiAccessible() throws Exception {
            mockMvc.perform(get("/swagger-ui/index.html"))
                   .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /v3/api-docs is public")
        void apiDocsAccessible() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                   .andExpect(status().isOk());
        }

        // Add similar tests for any other permitAll() paths
    }

    @Nested
    @DisplayName("Protected Endpoints")
    class ProtectedEndpoints {

        @Test
        @DisplayName("GET /api/orders without authentication returns 401")
        void ordersUnauthenticated() throws Exception {
            mockMvc.perform(get("/api/orders"))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST /api/orders with authentication but without CSRF returns 403")
        @WithMockUser
        void createOrderWithoutCsrf() throws Exception {
            mockMvc.perform(post("/api/orders"))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST /api/orders with authentication and CSRF returns 200")
        @WithMockUser
        void createOrderWithCsrf() throws Exception {
            mockMvc.perform(post("/api/orders").with(csrf()))
                   .andExpect(status().isOk());
        }

        // Repeat analogous tests for other HTTP methods and protected paths found in WebSecurityConfig
    }
}