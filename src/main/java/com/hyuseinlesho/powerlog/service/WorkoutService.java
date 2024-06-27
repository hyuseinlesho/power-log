package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;

import java.util.List;

public interface WorkoutService {
    void createWorkout(CreateWorkoutDto workoutDto);

    CreateWorkoutDto findWorkoutById(Long workoutId);

    List<Workout> findAllWorkouts();

    void editWorkout(CreateWorkoutDto workoutDto);

    void deleteWorkout(Long id);

    List<CreateWorkoutDto> searchWorkouts(String query);
}
