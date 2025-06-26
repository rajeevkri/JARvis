package com.example.JARvis.model;

import java.util.List;

public class TrainingExample {
    private String question;
    private String answer;
    private List<String> supportingContexts; // From your vector store

    // Constructors, getters, setters
    public TrainingExample() {}

    public TrainingExample(String question, String answer, List<String> supportingContexts) {
        this.question = question;
        this.answer = answer;
        this.supportingContexts = supportingContexts;
    }
}