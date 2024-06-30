package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;

import java.util.List;

public interface ExerciseService {
    void createExercise(CreateExerciseDto exerciseDto);

    CreateExerciseDto findExerciseById(Long exerciseId);

    List<CreateExerciseDto> findAllExercises();

    void editExercise(CreateExerciseDto exerciseDto);

    void deleteExercise(Long id);

    boolean addNewExercise(String name, ExerciseType type);
}
