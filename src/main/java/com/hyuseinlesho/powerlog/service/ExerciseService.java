package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;

import java.util.List;

public interface ExerciseService {
    void createExercise(CreateExerciseDto exerciseDto);

    CreateExerciseDto findExerciseById(Long exerciseId);

    List<CreateExerciseDto> findAllExercises();

    void editExercise(CreateExerciseDto exerciseDto);

    void deleteExercise(Long id);
}
