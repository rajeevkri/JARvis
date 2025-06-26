package com.example.JARvis.config;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Bean
    public OllamaApi ollamaApi() {
        return new OllamaApi("http://localhost:11434");
    }

    @Bean
    public EmbeddingClient embeddingClient(OllamaApi ollamaApi) {
        return new OllamaEmbeddingClient(ollamaApi)
                .withModel("llama3"); // or your preferred model
    }
}