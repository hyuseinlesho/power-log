package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;

import java.util.List;

public interface WorkoutService {
    void createWorkout(CreateWorkoutDto workoutDto);

    WorkoutDto findWorkoutById(Long workoutId);

    List<WorkoutDto> findAllWorkoutsSortedByDate();

    void editWorkout(CreateWorkoutDto workoutDto);

    void deleteWorkout(Long id);

    List<WorkoutDto> searchWorkouts(String query);

    List<Object> addWeekDelimiters(List<WorkoutDto> workouts);
}
