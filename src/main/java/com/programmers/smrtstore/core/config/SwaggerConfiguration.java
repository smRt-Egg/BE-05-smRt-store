package com.programmers.smrtstore.core.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER,
    name = "Authentication", description = "Prefix Required! Add 'Bearer ' before token!"
)
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
            .group("api")
            .addOpenApiCustomizer(customizer -> customizer.info(
                    new Info().title("smRt Store API").description("smRt Store API Documentation")
                        .version("alpha 1.0"))
                .security(List.of(new SecurityRequirement().addList("Authentication"))))
            .pathsToMatch("/**")
            .build();
    }

}
