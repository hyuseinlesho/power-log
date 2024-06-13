package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.Exercise;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }
}
