package com.estoque.veiculos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestão de estoque de veículos")
                        .description("API REST — use GET /api/veiculos com parâmetros de query para filtrar.")
                        .version("1.0"));
    }
}
