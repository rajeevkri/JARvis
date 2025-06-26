package com.example.JARvis.model;

import java.util.Map;

public class KnowledgeDocument {
    private String content;
    private Map<String, Object> metadata;

    // Constructors
    public KnowledgeDocument() {}

    public KnowledgeDocument(String content, Map<String, Object> metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}