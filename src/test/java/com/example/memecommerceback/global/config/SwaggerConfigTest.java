package com.example.memecommerceback.global.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;
import com.example.memecommerceback.global.config.SwaggerConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.assertj.core.api.Assertions.assertThat;

public class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    void apiDocket_shouldReturnConfiguredDocket() {
        Docket docket = swaggerConfig.apiDocket();
        assertThat(docket).isNotNull();
        assertThat(docket.getDocumentationType()).isEqualTo(DocumentationType.SWAGGER_2);
        assertThat(docket.getGroupName()).isEqualTo("MemeCommerce API");
    }

    @Test
    void apiDocket_shouldScanBasePackage() throws Exception {
        Docket docket = swaggerConfig.apiDocket();
        Field selectorField = Docket.class.getDeclaredField("selector");
        selectorField.setAccessible(true);
        Object selector = selectorField.get(docket);
        assertThat(selector.toString()).contains("com.example.memecommerceback");
    }

    @Test
    void apiInfo_shouldReturnProperApiInfo() throws Exception {
        Method apiInfoMethod = SwaggerConfig.class.getDeclaredMethod("apiInfo");
        apiInfoMethod.setAccessible(true);
        ApiInfo apiInfo = (ApiInfo) apiInfoMethod.invoke(swaggerConfig);
        assertThat(apiInfo.getTitle()).isEqualTo("MemeCommerce API Documentation");
        assertThat(apiInfo.getDescription()).isEqualTo("API Documentation for MemeCommerce application");
        assertThat(apiInfo.getVersion()).isEqualTo("1.0");
    }

    @Test
    void apiDocket_shouldIncludeApiInfo() throws Exception {
        Docket docket = swaggerConfig.apiDocket();
        Method apiInfoMethod = SwaggerConfig.class.getDeclaredMethod("apiInfo");
        apiInfoMethod.setAccessible(true);
        ApiInfo expected = (ApiInfo) apiInfoMethod.invoke(swaggerConfig);
        assertThat(docket.getApiInfo()).isEqualTo(expected);
    }
}