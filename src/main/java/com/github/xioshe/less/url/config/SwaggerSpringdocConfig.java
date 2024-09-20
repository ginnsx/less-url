package com.github.xioshe.less.url.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@RequiredArgsConstructor
public class SwaggerSpringdocConfig {

    private final BuildProperties buildProperties;

//    @Bean
//    @Profile("!prod")
//    public GroupedOpenApi actuatorApi(OpenApiCustomizer actuatorOpenApiCustomizer,
//                                      OperationCustomizer actuatorCustomizer,
//                                      WebEndpointProperties endpointProperties,
//                                      @Value("${springdoc.version}") String appVersion) {
//        return GroupedOpenApi.builder()
//                .group("Actuator")
//                .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
//                .addOpenApiCustomizer(actuatorOpenApiCustomizer)
//                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Actuator API").version(appVersion)))
//                .addOperationCustomizer(actuatorCustomizer)
//                .pathsToExclude("/health/*")
//                .build();
//    }

    //
//    @Bean
//    public GroupedOpenApi usersGroup() {
//        return GroupedOpenApi.builder()
//                .group("users")
//                .addOperationCustomizer((operation, handlerMethod) -> {
//                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
//                    return operation;
//                })
//                .addOpenApiCustomizer(openApi ->
//                        openApi.info(new Info().title("Users API").version(buildProperties.getVersion())))
//                .packagesToScan("com.github.xioshe.less.url.api")
//                .pathsToMatch("/users/**")
//                .build();
//    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(new Info()
                        .title("less-url API")
                        .version(buildProperties.getVersion()));
    }
}