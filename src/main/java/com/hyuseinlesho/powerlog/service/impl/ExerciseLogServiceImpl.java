package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.ExerciseLog;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.service.ExerciseLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseLogServiceImpl implements ExerciseLogService {
    private final ExerciseLogRepository exerciseLogRepository;

    public ExerciseLogServiceImpl(ExerciseLogRepository exerciseLogRepository) {
        this.exerciseLogRepository = exerciseLogRepository;
    }

    @Override
    public List<ExerciseLog> findAllExercises() {
        return exerciseLogRepository.findAll();
    }
}
