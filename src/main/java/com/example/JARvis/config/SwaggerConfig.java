package com.example.JARvis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JARvis RAG API")
                        .version("1.0")
                        .description("Retrieval-Augmented Generation API using Spring AI, Ollama and Milvus"));
    }
}