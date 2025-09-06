package com.estoque.controle.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customizarOpenAPI() {
        return  new OpenAPI()
                .info(new Info()
                        .title("API para controle de estoque")
                        .version("1.0")
                        .description("Documentação da API de controle de estoque," +
                                " o objetivo desse projeto é colocar em prática o que" +
                                " foi aprendido durante a faculdade e cursos por fora." +
                                "Esta API ajuda no controle de estoque e com proteção de rotas e níveis do usuário e banco de dados MySQL."));
    }
}
