package com.example.memecommerceback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memecommerceback.MemeController;
import com.example.memecommerceback.MemeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MemeCommerceBackApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Sanity check that the Spring application context loads successfully.
     */
    @Test
    void contextLoads() {
    }

    /**
     * Verify that the application context contains the expected beans.
     */
    @Test
    void contextContainsExpectedBeans() {
        assertThat(applicationContext.getBean("memeCommerceBackApplication")).isNotNull();
        assertThat(applicationContext.getBean(MemeController.class)).isNotNull();
        assertThat(applicationContext.getBean(MemeService.class)).isNotNull();
    }

    /**
     * Test that the root endpoint ("/") returns HTTP 200 OK.
     */
    @Test
    void rootEndpointShouldReturn200() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }

    /**
     * Test that an unmapped endpoint returns HTTP 404 Not Found.
     */
    @Test
    void unknownEndpointShouldReturn404() throws Exception {
        mockMvc.perform(get("/does-not-exist"))
               .andExpect(status().isNotFound());
    }
}