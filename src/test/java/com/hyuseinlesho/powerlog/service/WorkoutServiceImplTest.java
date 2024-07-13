package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.exception.WorkoutNotFoundException;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import com.hyuseinlesho.powerlog.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private ExerciseLogRepository exerciseLogRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Test
    void createWorkout() {
        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name("Squat").sets(3).reps(5).weight(80.0).build();
        List<ExerciseLogDto> exercises = List.of(logDto);

        ExerciseLog exerciseLog = ExerciseLog.builder()
                .name(logDto.getName())
                .sets(logDto.getSets())
                .reps(logDto.getReps())
                .weight(logDto.getWeight()).build();

        CreateWorkoutDto workoutDto = CreateWorkoutDto.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00")
                .exercises(exercises).build();

        Workout workout = Workout.builder()
                .title(workoutDto.getTitle())
                .date(workoutDto.getDate())
                .time(workoutDto.getTime())
                .exercises(List.of(exerciseLog)).build();

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(workoutMapper.mapToWorkout(workoutDto)).thenReturn(workout);
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutRepository.save(workout)).thenReturn(workout);

        workoutService.createWorkout(workoutDto);

        verify(workoutMapper, times(1)).mapToWorkout(workoutDto);
        verify(userService, times(1)).getCurrentUser();
        verify(workoutRepository, times(1)).save(workout);

        assertEquals(user, workout.getUser());

        for (ExerciseLog exercise : workout.getExercises()) {
            assertEquals(workout, exercise.getWorkout());
        }

        double expectedTotalVolume = logDto.getSets() * logDto.getReps() * logDto.getWeight();
        assertEquals(expectedTotalVolume, workout.getTotalVolume());
    }

    @Test
    void findWorkoutById_WorkoutDoesNotExist_ThrowsWorkoutNotFoundException() {
        Long workoutId = 1L;

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> {
            workoutService.findWorkoutById(workoutId);
        });
        verify(workoutMapper, never()).mapToWorkoutDto(any(Workout.class));
    }

    @Test
    void findWorkoutById_WorkoutExists_ReturnsWorkoutDto() {
        Long workoutId = 1L;

        ExerciseLog exerciseLog = ExerciseLog.builder()
                .name("Squat").sets(3).reps(5).weight(80.0).build();
        List<ExerciseLog> exercises = List.of(exerciseLog);

        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name(exerciseLog.getName())
                .sets(exerciseLog.getSets())
                .reps(exerciseLog.getReps())
                .weight(exerciseLog.getWeight()).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        Workout workout = Workout.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00")
                .exercises(exercises).build();

        WorkoutDto workoutDto = WorkoutDto.builder()
                .title(workout.getTitle())
                .date(workout.getDate())
                .time(workout.getTime())
                .exercises(exerciseDtos).build();

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
        when(workoutMapper.mapToWorkoutDto(workout)).thenReturn(workoutDto);

        WorkoutDto result = workoutService.findWorkoutById(workoutId);

        assertNotNull(result);
        assertEquals(workoutDto.getTitle(), result.getTitle());
        assertEquals(workoutDto.getDate(), result.getDate());
        assertEquals(workoutDto.getTime(), result.getTime());
        assertEquals(workoutDto.getExercises(), result.getExercises());

        verify(workoutRepository, times(1)).findById(workoutId);
        verify(workoutMapper, times(1)).mapToWorkoutDto(workout);
    }
}
