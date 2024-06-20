package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;

import java.util.List;

public interface WorkoutService {
    List<Workout> findWorkoutsByUsername(String username);

    void createWorkout(WorkoutDto workoutDto, String username);

    WorkoutDto findWorkoutById(Long workoutId);

    void editWorkout(WorkoutDto workoutDto, String username);

    void deleteWorkout(Long id);

    List<WorkoutDto> searchWorkoutsForUser(String username, String query);
}
