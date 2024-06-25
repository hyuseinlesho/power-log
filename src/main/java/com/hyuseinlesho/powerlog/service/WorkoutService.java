package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;

import java.util.List;

public interface WorkoutService {
    void createWorkout(WorkoutDto workoutDto);

    WorkoutDto findWorkoutById(Long workoutId);

    List<Workout> findAllWorkouts();

    void editWorkout(WorkoutDto workoutDto);

    void deleteWorkout(Long id);

    List<WorkoutDto> searchWorkouts(String query);
}
