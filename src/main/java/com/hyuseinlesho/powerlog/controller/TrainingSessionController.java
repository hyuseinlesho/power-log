package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.service.TrainingSessionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-sessions")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }
}
