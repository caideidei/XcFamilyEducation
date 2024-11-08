package com.example.familyeducation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("swagger文档标题")
                        .description("swagger文档注释")
                        .version("1.0")
                        .termsOfService("服务地址"));
    }
}
