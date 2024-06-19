package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.Exercise;

import java.util.List;

public interface ExerciseService {

    void createExercise(ExerciseDto exerciseDto, String username);

    List<ExerciseDto> findAllExercisesForUser(String username);

    ExerciseDto findExerciseById(Long exerciseId);

    void editExercise(ExerciseDto exerciseDto, String username);

    void deleteExercise(Long id);
}
