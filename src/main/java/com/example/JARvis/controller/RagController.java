package com.example.JARvis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RagController {

    private final OllamaChatClient chatClient;
    private final VectorStore vectorStore;

    public RagController(OllamaChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Operation(
            summary = "Ask a question",
            description = "Get an AI-generated answer based on retrieved documents",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(
                                            value = "Paris is the capital of France"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/ask")
    public String ask(
            @Parameter(
                    description = "The question to answer",
                    example = "What is the capital of France?",
                    required = true
            )
            @RequestParam String question) {

        List<Document> relevantDocs = vectorStore.similaritySearch(question);

        String context = relevantDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate("""
            Answer the question based only on the following context.
            If you don't know the answer, say "I don't know".
            
            Context:
            {context}
            
            Question: {question}
            Answer:""");

        Prompt prompt = promptTemplate.create(Map.of(
                "context", context,
                "question", question
        ));

        ChatResponse response = chatClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }
}