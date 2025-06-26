package com.example.JARvis.service;

import com.example.JARvis.model.KnowledgeDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KnowledgeService {

    private final VectorStore vectorStore;
    private final EmbeddingClient embeddingClient;

    public KnowledgeService(VectorStore vectorStore, EmbeddingClient embeddingClient) {
        this.vectorStore = vectorStore;
        this.embeddingClient = embeddingClient;
    }

    public void processAndStoreKnowledge(List<KnowledgeDocument> documents) {
        List<Document> aiDocs = documents.stream()
                .map(doc -> new Document(doc.getContent(), doc.getMetadata()))
                .collect(Collectors.toList());

        vectorStore.add(aiDocs);
    }

    public List<KnowledgeDocument> retrieveRelevantKnowledge(String query, int topK) {
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.query(query).withTopK(topK)
        );

        return results.stream()
                .map(doc -> new KnowledgeDocument(
                        doc.getContent(),
                        doc.getMetadata()
                ))
                .collect(Collectors.toList());
    }
}