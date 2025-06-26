package com.example.JARvis.config;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Bean
    public OllamaChatClient ollamaChatClient() {
        // Create the API client first
        OllamaApi ollamaApi = new OllamaApi("http://localhost:11434");

        // Then create the chat client
        OllamaChatClient chatClient = new OllamaChatClient(ollamaApi);

        // Set the model
        chatClient.withModel("llama3");

        return chatClient;
    }
}
