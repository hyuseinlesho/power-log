package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.repository.TrainingSessionRepository;
import com.hyuseinlesho.powerlog.service.TrainingSessionService;
import org.springframework.stereotype.Service;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;

    public TrainingSessionServiceImpl(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }
}
