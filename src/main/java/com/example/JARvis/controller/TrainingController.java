package com.example.JARvis.controller;

import com.example.JARvis.service.ModelTrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    private final ModelTrainingService modelTrainingService;

    public TrainingController(ModelTrainingService modelTrainingService) {
        this.modelTrainingService = modelTrainingService;
    }

    @PostMapping("/fine-tune")
    public ResponseEntity<String> fineTuneModel(
            @RequestParam String baseModel,
            @RequestBody List<String> trainingQueries) {
        modelTrainingService.fineTuneModel(baseModel, trainingQueries);
        return ResponseEntity.ok("Fine-tuning started");
    }
}