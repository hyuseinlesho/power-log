package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;

import java.util.List;

public interface ExerciseService {
    Exercise createExercise(CreateExerciseDto exerciseDto);
    boolean addNewExercise(String name, ExerciseType type);

    CreateExerciseDto findExerciseById(Long exerciseId);

    List<ExerciseDto> findAllExercises();

    Exercise updateExercise(Long id, UpdateExerciseDto exerciseDto);

    void deleteExercise(Long id);
}
