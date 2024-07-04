package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseLogDto;
import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import com.hyuseinlesho.powerlog.security.SecurityUtil;
import com.hyuseinlesho.powerlog.service.UserService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ExerciseLogRepository exerciseLogRepository, UserService userService, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseLogRepository = exerciseLogRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void createWorkout(CreateWorkoutDto workoutDto) {
        Workout workout = WorkoutMapper.INSTANCE.mapToWorkout(workoutDto);
        workout.setUser(userService.getCurrentUser());

        List<ExerciseLog> exercises = workout.getExercises();
        for (ExerciseLog exercise : exercises) {
            exercise.setWorkout(workout);
        }

        workoutRepository.save(workout);
    }

    @Override
    public CreateWorkoutDto findWorkoutById(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId).get();
        return WorkoutMapper.INSTANCE.mapToWorkoutDto(workout);
    }

    @Override
    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAllByUser(userService.getCurrentUser());
    }

    @Override
    public void editWorkout(CreateWorkoutDto workoutDto) {
        Workout workout = workoutRepository.findById(workoutDto.getId()).get();
        workout.setTitle(workoutDto.getTitle());

        workout.setDate(workoutDto.getDate());
        workout.setTime(workoutDto.getTime());

        List<ExerciseLog> exercises = exerciseLogRepository.findAllByWorkout(workout);
        List<CreateExerciseLogDto> exerciseDtos = workoutDto.getExercises();

        int minSize = Math.min(exercises.size(), exerciseDtos.size());
        for (int i = 0; i < minSize; i++) {
            ExerciseLog exerciseLog = exercises.get(i);
            CreateExerciseLogDto exerciseDto = exerciseDtos.get(i);

            exerciseLog.setName(exerciseDto.getName());
            exerciseLog.setSets(exerciseDto.getSets());
            exerciseLog.setReps(exerciseDto.getReps());
            exerciseLog.setWeight(exerciseDto.getWeight());
        }

        if (exerciseDtos.size() > exercises.size()) {
            for (int i = exercises.size(); i < exerciseDtos.size(); i++) {
                CreateExerciseLogDto exerciseDto = exerciseDtos.get(i);
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
    public List<CreateWorkoutDto> searchWorkouts(String query) {
        String username = SecurityUtil.getSessionUser();

        List<Workout> workouts = workoutRepository.findByUserAndSearchQuery(username, query);
        return workouts.stream()
                .map(WorkoutMapper.INSTANCE::mapToWorkoutDto)
                .collect(Collectors.toList());
    }
}
