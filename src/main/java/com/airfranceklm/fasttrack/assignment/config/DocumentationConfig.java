package com.airfranceklm.fasttrack.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * DocumentationConfig is a Spring configuration class that sets up Swagger 2 for API documentation.
 *
 * <p>This class defines the necessary configuration to integrate Swagger with a Spring Boot application.
 * Swagger generates interactive API documentation for the application's RESTful web services.
 * Developers can use this documentation to explore the available endpoints, their parameters, and expected responses.
 *
 * <p>The configuration is done through a {@link Docket} bean, which allows customization of the endpoints
 * and paths that will be included in the generated Swagger documentation.
 */
@Configuration
public class DocumentationConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
