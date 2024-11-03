package com.github.xioshe.less.url.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@RequiredArgsConstructor
public class SwaggerSpringdocConfig {

    @Value("${version}")
    private String version;

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

//    @Bean
//    public GlobalOpenApiCustomizer consumerTypeHeaderOpenAPICustomizer() {
//        return openApi -> openApi.getPaths().values().stream()
//                .flatMap(pathItem -> pathItem.readOperations().stream())
//                .forEach(operation -> {
//                    var parameter = new HeaderParameter()
//                            .name(CustomHeaders.GUEST_ID)
//                            .description("游客 id");
//                    operation.addParametersItem(parameter);
//                });
//    }

    @Bean
    public OpenAPI customOpenAPI() {
        SpringDocUtils.getConfig()
                .replaceWithClass(Date.class, Long.class)
                .replaceWithClass(LocalDateTime.class, Long.class)
                .replaceWithClass(LocalDate.class, Long.class)
                .replaceWithClass(LocalTime.class, Long.class);
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(new Info()
                        .title("less-url API")
                        .description("less-url API 文档，部分接口需要 JWT Token，请通过 /auth/token 接口获取。")
                        .version(version));
    }
}
