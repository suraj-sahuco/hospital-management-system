package com.codingshuttle.youtube.hospitalManagement.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customAPI(){

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Fitness Tracking API")
                        .version("v1.0")
                        .description("Production Grade API'S")
                        .contact(new Contact()
                                .name("suraj")
                                .url("https://suraj.com")
                                .email("suraj@321.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://Google.com")))

                // 🔥 ADD THIS PART
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }//http://localhost:8080/api/v1/swagger-ui/index.html
}

