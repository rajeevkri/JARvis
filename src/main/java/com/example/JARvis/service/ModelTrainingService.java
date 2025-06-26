package com.example.JARvis.service;

import com.example.JARvis.model.KnowledgeDocument;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ModelTrainingService {

    private final KnowledgeService knowledgeService;

    public ModelTrainingService(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    public void fineTuneModel(String baseModel, List<String> trainingQueries) {
        Map<String, List<KnowledgeDocument>> trainingData = new HashMap<>();
        for (String query : trainingQueries) {
            trainingData.put(query, knowledgeService.retrieveRelevantKnowledge(query, 5));
        }

        List<Example> examples = prepareTrainingExamples(trainingData);
        // Implementation would continue with actual model training
    }

    private List<Example> prepareTrainingExamples(Map<String, List<KnowledgeDocument>> data) {
        // Convert to training examples format
        return data.entrySet().stream()
                .map(entry -> new Example(entry.getKey(),
                        entry.getValue().stream()
                                .map(KnowledgeDocument::getContent)
                                .collect(Collectors.joining("\n\n"))
                ))
                .collect(Collectors.toList());
    }

    public static class Example {
        private final String query;
        private final String context;

        public Example(String query, String context) {
            this.query = query;
            this.context = context;
        }

        // Getters
        public String getQuery() { return query; }
        public String getContext() { return context; }
    }
}