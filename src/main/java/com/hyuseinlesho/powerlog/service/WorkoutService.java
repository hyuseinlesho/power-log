package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.entity.Workout;

import java.util.List;

public interface WorkoutService {
    void createWorkout(CreateWorkoutDto workoutDto);

    WorkoutDto findWorkoutById(Long workoutId);

    List<Workout> findAllWorkouts();

    void editWorkout(CreateWorkoutDto workoutDto);

    void deleteWorkout(Long id);

    List<WorkoutDto> searchWorkouts(String query);
}
