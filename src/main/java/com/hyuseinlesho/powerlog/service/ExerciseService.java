package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.Exercise;

import java.util.List;

public interface ExerciseService {
    void createExercise(ExerciseDto exerciseDto);

    ExerciseDto findExerciseById(Long exerciseId);

    List<ExerciseDto> findAllExercises();

    void editExercise(ExerciseDto exerciseDto);

    void deleteExercise(Long id);
}
