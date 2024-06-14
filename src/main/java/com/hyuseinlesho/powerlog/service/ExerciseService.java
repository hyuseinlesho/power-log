package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.Exercise;

import java.util.List;

public interface ExerciseService {
    List<Exercise> findAllExercises();

    void createExercise(ExerciseDto exerciseDto, String username);

    List<Exercise> findAllExercisesForUser(String username);
}
