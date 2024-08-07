package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.exception.WorkoutNotFoundException;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import com.hyuseinlesho.powerlog.service.UserService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserService userService;

    public WorkoutServiceImpl(
            WorkoutRepository workoutRepository,
            WorkoutMapper workoutMapper,
            ExerciseLogRepository exerciseLogRepository,
            UserService userService
    ) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.exerciseLogRepository = exerciseLogRepository;
        this.userService = userService;
    }

    @Override
    public void createWorkout(CreateWorkoutDto workoutDto) {
        Workout workout = workoutMapper.mapToWorkout(workoutDto);
        workout.setUser(userService.getCurrentUser());

        List<ExerciseLog> exercises = workout.getExercises();
        for (ExerciseLog exercise : exercises) {
            exercise.setWorkout(workout);
        }

        workout.setTotalVolume(calculateTotalVolume(workout.getExercises()));
        workoutRepository.save(workout);
    }

    @Override
    public WorkoutDto getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout not found for id: " + id));
        ;
        return workoutMapper.mapToWorkoutDto(workout);
    }

    @Override
    public List<WorkoutDto> getAllWorkoutsSortedByDate() {
        return workoutRepository.findAllByUserOrderByDateAsc(userService.getCurrentUser())
                .stream()
                .map(workoutMapper::mapToWorkoutDto)
                .toList();
    }

    @Override
    public void updateWorkout(UpdateWorkoutDto workoutDto) {
        Workout workout = workoutRepository.findById(workoutDto.getId())
                .orElseThrow(() -> new WorkoutNotFoundException("Workout not found for id: " + workoutDto.getId()));
        workout.setTitle(workoutDto.getTitle());

        workout.setDate(workoutDto.getDate());
        workout.setTime(workoutDto.getTime());

        List<ExerciseLog> exercises = exerciseLogRepository.findAllByWorkout(workout);
        List<ExerciseLogDto> exerciseDtos = workoutDto.getExercises();

        for (int i = 0; i < exercises.size(); i++) {
            ExerciseLog exerciseLog = exercises.get(i);
            ExerciseLogDto exerciseDto = exerciseDtos.get(i);

            exerciseLog.setName(exerciseDto.getName());
            exerciseLog.setSets(exerciseDto.getSets());
            exerciseLog.setReps(exerciseDto.getReps());
            exerciseLog.setWeight(exerciseDto.getWeight());
        }

        workout.setExercises(exercises);
        workout.setTotalVolume(calculateTotalVolume(exercises));
        workout.setComment(workoutDto.getComment());

        workoutRepository.save(workout);
    }

    @Override
    public void deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new WorkoutNotFoundException("Workout not found for id: " + id);
        }

        workoutRepository.deleteById(id);
    }

    @Override
    public List<WorkoutDto> searchWorkouts(String query) {
        List<Workout> workouts = workoutRepository.findAllByUserAndSearchQuery(userService.getCurrentUser(), query);
        return workouts.stream()
                .map(workoutMapper::mapToWorkoutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> addWeekDelimiters(List<WorkoutDto> workouts) {
        List<Object> result = new ArrayList<>();
        int currentWeek = -1;

        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);

        for (WorkoutDto workout : workouts) {
            LocalDate date = workout.getDate();

            int weekOfYear = date.get(weekFields.weekOfWeekBasedYear());

            if (weekOfYear != currentWeek) {
                LocalDate startOfWeek = date.with(weekFields.dayOfWeek(), 1);
                result.add("Week starting on: " + startOfWeek);
                currentWeek = weekOfYear;
            }
            result.add(workout);
        }

        return result;
    }


    private double calculateTotalVolume(List<ExerciseLog> exercises) {
        return exercises.stream()
                .mapToDouble(exercise -> exercise.getSets() * exercise.getReps() * exercise.getWeight())
                .sum();
    }
}
