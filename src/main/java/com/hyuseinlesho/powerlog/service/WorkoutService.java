package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;

import java.util.List;

public interface WorkoutService {
    List<Workout> findWorkoutsByUsername(String username);

    void createWorkout(WorkoutDto workoutDto);
}
