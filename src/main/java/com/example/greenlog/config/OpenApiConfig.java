package com.example.greenlog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI greenLogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GreenLog API")
                        .version("1.0")
                        .description("REST API for tracking tree plantation drives, volunteers, trees, and survival check-ins."));
    }
}
