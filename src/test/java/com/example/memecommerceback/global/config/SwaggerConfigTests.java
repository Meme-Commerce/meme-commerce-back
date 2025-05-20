package com.example.memecommerceback.global.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;

import com.example.memecommerceback.global.config.SwaggerConfig;

@ExtendWith(SpringExtension.class)
@Import(SwaggerConfig.class)
public class SwaggerConfigTests {

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Nested
    @DisplayName("Docket Bean Creation")
    class DocketBeanTests {

        @Test
        @DisplayName("should create a non-null Docket bean with SWAGGER_2 documentation type")
        void shouldCreateDocketBeanWithSwagger2() {
            Docket docket = swaggerConfig.docket();
            assertThat(docket).isNotNull();
            assertThat(docket.getDocumentationType())
                .isEqualTo(DocumentationType.SWAGGER_2);
        }
    }

    @Nested
    @DisplayName("ApiInfo Configuration")
    class ApiInfoTests {

        @Test
        @DisplayName("should configure ApiInfo with correct title, description, and version")
        void shouldConfigureApiInfoCorrectly() {
            ApiInfo apiInfo = swaggerConfig.docket().getApiInfo();
            assertThat(apiInfo.getTitle())
                .isEqualTo("EXPECTED_TITLE_FROM_CONFIG");
            assertThat(apiInfo.getDescription())
                .isEqualTo("EXPECTED_DESCRIPTION_FROM_CONFIG");
            assertThat(apiInfo.getVersion())
                .isEqualTo("EXPECTED_VERSION_FROM_CONFIG");
        }
    }

    @Nested
    @DisplayName("API Selector Configuration")
    class SelectorTests {

        @Test
        @DisplayName("should scan the application's base package")
        void shouldScanBasePackage() {
            Docket docket = swaggerConfig.docket();
            // Adjust inspection logic based on actual Docket selector implementation
            String basePackage = docket
                .getDocumentationType() // replace with real selector introspection
                .toString();
            assertThat(basePackage)
                .contains("com.example.memecommerceback");
        }
    }
}