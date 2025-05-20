package com.example.memecommerceback.global.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;

import com.example.memecommerceback.global.config.SwaggerConfig;

@DisplayName("SwaggerConfig unit tests")
public class SwaggerConfigTests {

    private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    @DisplayName("apiDocket() should return non-null Docket")
    void apiDocketIsNotNull() {
        Docket docket = swaggerConfig.apiDocket();
        assertThat(docket).isNotNull();
    }

    @Test
    @DisplayName("apiDocket() should use SWAGGER_2 documentation type")
    void apiDocketHasSwagger2Type() {
        Docket docket = swaggerConfig.apiDocket();
        assertThat(docket.getDocumentationType()).isEqualTo(DocumentationType.SWAGGER_2);
    }

    @Test
    @DisplayName("apiDocket() should scan the application base package and include all paths")
    void apiDocketScansBasePackageAndAllPaths() {
        Docket docket = swaggerConfig.apiDocket();
        // verify selector picks up controllers in our base package
        assertThat(docket.getDocketContext()
                         .getDocumentationContext()
                         .getApiSelector()
                         .getApis())
            .anyMatch(p -> p.apply(RequestHandlerSelectors.basePackage("com.example.memecommerceback")));
        // verify selector includes all paths
        assertThat(docket.getDocketContext()
                         .getDocumentationContext()
                         .getApiSelector()
                         .getPaths())
            .anyMatch(p -> p.apply(PathSelectors.any()));
    }

    @Test
    @DisplayName("apiDocket() should configure correct ApiInfo metadata")
    void apiDocketHasCorrectApiInfo() {
        Docket docket = swaggerConfig.apiDocket();
        ApiInfo info = docket.getApiInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("Meme Commerce API");
        assertThat(info.getDescription()).contains("API for meme commerce");
        assertThat(info.getVersion()).isEqualTo("1.0.0");
    }
}