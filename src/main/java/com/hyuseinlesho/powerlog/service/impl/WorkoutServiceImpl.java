package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import com.hyuseinlesho.powerlog.model.ExerciseLog;
import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserRepository userRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ExerciseLogRepository exerciseLogRepository, UserRepository userService) {
        this.workoutRepository = workoutRepository;
        this.exerciseLogRepository = exerciseLogRepository;
        this.userRepository = userService;
    }

    @Override
    public List<Workout> findWorkoutsByUsername(String username) {
        return workoutRepository.findByUser(getUser(username));
    }

    @Override
    public void createWorkout(WorkoutDto workoutDto, String username) {
        Workout workout = WorkoutMapper.INSTANCE.workoutDtoToWorkout(workoutDto);
        workout.setUser(getUser(username));

        List<ExerciseLog> exercises = workout.getExercises();
        for (ExerciseLog exercise : exercises) {
            exercise.setWorkout(workout);
        }

        workoutRepository.save(workout);
    }

    @Override
    public WorkoutDto findWorkoutById(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId).get();
        return WorkoutMapper.INSTANCE.workoutToWorkoutDto(workout);
    }

    @Override
    public void editWorkout(WorkoutDto workoutDto, String username) {
        Workout workout = workoutRepository.findById(workoutDto.getId()).get();
        workout.setTitle(workoutDto.getTitle());
        workout.setDate(workoutDto.getDate());

        List<ExerciseLog> exercises = exerciseLogRepository.findAllByWorkout(workout);
        List<ExerciseLogDto> exerciseDtos = workoutDto.getExercises();

        int minSize = Math.min(exercises.size(), exerciseDtos.size());
        for (int i = 0; i < minSize; i++) {
            ExerciseLog exerciseLog = exercises.get(i);
            ExerciseLogDto exerciseDto = exerciseDtos.get(i);

            exerciseLog.setName(exerciseDto.getName());
            exerciseLog.setSets(exerciseDto.getSets());
            exerciseLog.setReps(exerciseDto.getReps());
            exerciseLog.setWeight(exerciseDto.getWeight());
        }

        if (exerciseDtos.size() > exercises.size()) {
            for (int i = exercises.size(); i < exerciseDtos.size(); i++) {
                ExerciseLogDto exerciseDto = exerciseDtos.get(i);
                ExerciseLog newExerciseLog = new ExerciseLog();

                newExerciseLog.setWorkout(workout);
                newExerciseLog.setName(exerciseDto.getName());
                newExerciseLog.setSets(exerciseDto.getSets());
                newExerciseLog.setReps(exerciseDto.getReps());
                newExerciseLog.setWeight(exerciseDto.getWeight());
                exercises.add(newExerciseLog);
            }
        }

        workout.setExercises(exercises);
        workout.setComment(workoutDto.getComment());

        workoutRepository.save(workout);
    }

    @Override
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }

    @Override
    public List<WorkoutDto> searchWorkoutsForUser(String username, String query) {
        List<Workout> workouts = workoutRepository.findByUserAndSearchQuery(username, query);
        return workouts.stream()
                .map(WorkoutMapper.INSTANCE::workoutToWorkoutDto)
                .collect(Collectors.toList());
    }

    private UserEntity getUser(String username) {
        return userRepository.findByUsername(username);
    }
}
