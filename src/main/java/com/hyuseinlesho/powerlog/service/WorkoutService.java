package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;

import java.util.List;

public interface WorkoutService {
    void createWorkout(CreateWorkoutDto workoutDto);

    WorkoutDto getWorkoutById(Long workoutId);

    List<WorkoutDto> getAllWorkoutsSortedByDate();

    void editWorkout(UpdateWorkoutDto workoutDto);

    void deleteWorkout(Long id);

    List<WorkoutDto> searchWorkouts(String query);

    List<Object> addWeekDelimiters(List<WorkoutDto> workouts);
}
