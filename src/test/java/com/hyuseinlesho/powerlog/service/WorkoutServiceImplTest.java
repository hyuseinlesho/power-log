package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.exception.WorkoutNotFoundException;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import com.hyuseinlesho.powerlog.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setUsername("test_user");
    }

    private static Workout createWorkout() {
        return Workout.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00").build();
    }

    private static ExerciseLog createExerciseLog() {
        return ExerciseLog.builder()
                .name("Squat").sets(3).reps(5).weight(80.0).build();
    }

    @Test
    void createWorkout_SaveWorkout() {
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
            workoutService.getWorkoutById(workoutId);
        });
        verify(workoutMapper, never()).mapToWorkoutDto(any(Workout.class));
    }

    @Test
    void findWorkoutById_WorkoutExists_ReturnsWorkoutDto() {
        Long workoutId = 1L;

        ExerciseLog exerciseLog = createExerciseLog();
        List<ExerciseLog> exercises = List.of(exerciseLog);

        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name(exerciseLog.getName())
                .sets(exerciseLog.getSets())
                .reps(exerciseLog.getReps())
                .weight(exerciseLog.getWeight()).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        Workout workout = createWorkout();
        workout.setExercises(exercises);

        WorkoutDto workoutDto = WorkoutDto.builder()
                .title(workout.getTitle())
                .date(workout.getDate())
                .time(workout.getTime())
                .exercises(exerciseDtos).build();

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
        when(workoutMapper.mapToWorkoutDto(workout)).thenReturn(workoutDto);

        WorkoutDto result = workoutService.getWorkoutById(workoutId);

        assertNotNull(result);
        assertEquals(workoutDto.getTitle(), result.getTitle());
        assertEquals(workoutDto.getDate(), result.getDate());
        assertEquals(workoutDto.getTime(), result.getTime());
        assertEquals(workoutDto.getExercises(), result.getExercises());

        verify(workoutRepository, times(1)).findById(workoutId);
        verify(workoutMapper, times(1)).mapToWorkoutDto(workout);
    }

    @Test
    void findAllWorkoutsSortedByDate_ReturnsWorkoutDtoList() {
        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name("Squat").sets(3).reps(5).weight(80.0).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        ExerciseLog exerciseLog = ExerciseLog.builder()
                .name(logDto.getName())
                .sets(logDto.getSets())
                .reps(logDto.getReps())
                .weight(logDto.getWeight()).build();
        List<ExerciseLog> exercises = List.of(exerciseLog);

        WorkoutDto workoutDto1 = WorkoutDto.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00")
                .exercises(exerciseDtos).build();

        WorkoutDto workoutDto2 = WorkoutDto.builder()
                .title("Workout B")
                .date(LocalDate.of(2024, 1, 3))
                .time("17:00")
                .exercises(exerciseDtos).build();

        Workout workout1 = Workout.builder()
                .title(workoutDto1.getTitle())
                .date(workoutDto1.getDate())
                .time(workoutDto1.getTime())
                .exercises(exercises).build();

        Workout workout2 = Workout.builder()
                .title(workoutDto2.getTitle())
                .date(workoutDto2.getDate())
                .time(workoutDto2.getTime())
                .exercises(exercises).build();

        List<Workout> workouts = List.of(workout1, workout2);

        when(workoutRepository.findAllByUserOrderByDateAsc(user)).thenReturn(workouts);
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutMapper.mapToWorkoutDto(workout1)).thenReturn(workoutDto1);
        when(workoutMapper.mapToWorkoutDto(workout2)).thenReturn(workoutDto2);

        List<WorkoutDto> result = workoutService.getAllWorkoutsSortedByDate();

        assertNotNull(result);
        assertEquals(workoutDto1.getTitle(), result.get(0).getTitle());
        assertEquals(workoutDto1.getDate(), result.get(0).getDate());
        assertEquals(workoutDto1.getTime(), result.get(0).getTime());
        assertEquals(workoutDto1.getExercises(), result.get(0).getExercises());

        assertEquals(workoutDto2.getTitle(), result.get(1).getTitle());
        assertEquals(workoutDto2.getDate(), result.get(1).getDate());
        assertEquals(workoutDto2.getTime(), result.get(1).getTime());
        assertEquals(workoutDto2.getExercises(), result.get(1).getExercises());

        verify(workoutRepository, times(1)).findAllByUserOrderByDateAsc(user);
        verify(userService, times(1)).getCurrentUser();
        verify(workoutMapper, times(1)).mapToWorkoutDto(workout1);
        verify(workoutMapper, times(1)).mapToWorkoutDto(workout2);
    }

    @Test
    void findAllWorkoutsSortedByDate_NoWorkouts_ReturnsEmptyList() {
        when(workoutRepository.findAllByUserOrderByDateAsc(user)).thenReturn(Collections.emptyList());
        when(userService.getCurrentUser()).thenReturn(user);

        List<WorkoutDto> result = workoutService.getAllWorkoutsSortedByDate();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(workoutRepository, times(1)).findAllByUserOrderByDateAsc(user);
        verify(userService, times(1)).getCurrentUser();
        verify(workoutMapper, never()).mapToWorkoutDto(any(Workout.class));
    }

    @Test
    void editWorkout_WorkoutDoesNotExist_ThrowsWorkoutNotFoundException() {
        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name("Squat").sets(5).reps(5).weight(60.0).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        UpdateWorkoutDto workoutDto = UpdateWorkoutDto.builder()
                .title("Workout B")
                .date(LocalDate.of(2024, 1, 5))
                .time("16:30")
                .exercises(exerciseDtos).build();
        workoutDto.setId(2L);

        assertThrows(WorkoutNotFoundException.class, () -> {
            workoutService.updateWorkout(workoutDto);
        });
        verify(workoutRepository, times(1)).findById(workoutDto.getId());
    }

    @Test
    void editWorkout_WorkoutExists_UpdateWorkout() {
        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name("Squat").sets(5).reps(5).weight(60.0).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        ExerciseLog exerciseLog = ExerciseLog.builder()
                .name(logDto.getName())
                .sets(logDto.getSets())
                .reps(logDto.getReps())
                .weight(logDto.getWeight()).build();
        List<ExerciseLog> exercises = List.of(exerciseLog);

        UpdateWorkoutDto workoutDto = UpdateWorkoutDto.builder()
                .title("Workout B")
                .date(LocalDate.of(2024, 1, 5))
                .time("16:30")
                .exercises(exerciseDtos).build();
        workoutDto.setId(1L);

        Workout workout = Workout.builder()
                .title(workoutDto.getTitle())
                .date(workoutDto.getDate())
                .time(workoutDto.getTime())
                .exercises(exercises).build();

        when(workoutRepository.findById(workoutDto.getId())).thenReturn(Optional.ofNullable(workout));
        when(exerciseLogRepository.findAllByWorkout(workout)).thenReturn(exercises);

        workoutService.updateWorkout(workoutDto);

        verify(workoutRepository, times(1)).findById(workoutDto.getId());
        verify(exerciseLogRepository, times(1)).findAllByWorkout(workout);
        assert workout != null;
        verify(workoutRepository, times(1)).save(workout);
    }

    @Test
    void deleteWorkout_WorkoutDoesNotExist_ThrowsWorkoutNotFoundException() {
        Long workoutId = 2L;

        when(workoutRepository.existsById(workoutId)).thenReturn(false);

        assertThrows(WorkoutNotFoundException.class, () -> {
            workoutService.deleteWorkout(workoutId);
        });

        verify(workoutRepository, times(1)).existsById(workoutId);
        verify(workoutRepository, never()).deleteById(workoutId);
    }

    @Test
    void deleteWorkout_WorkoutExists_DeleteById() {
        Long workoutId = 2L;

        when(workoutRepository.existsById(workoutId)).thenReturn(true);
        doNothing().when(workoutRepository).deleteById(workoutId);

        workoutService.deleteWorkout(workoutId);

        verify(workoutRepository, times(1)).existsById(workoutId);
        verify(workoutRepository, times(1)).deleteById(workoutId);
    }

    @Test
    void searchWorkouts_WorkoutsNotFound_ReturnsEmptyList() {
        String query = "Workout A";

        when(workoutRepository.findAllByUserAndSearchQuery(user, query))
                .thenReturn(Collections.emptyList());
        when(userService.getCurrentUser()).thenReturn(user);

        List<WorkoutDto> result = workoutService.searchWorkouts(query);

        assertEquals(result, List.of());

        verify(workoutRepository, times(1)).findAllByUserAndSearchQuery(user, query);
        verify(userService, times(1)).getCurrentUser();
        verify(workoutMapper, never()).mapToWorkoutDto(any(Workout.class));
    }

    @Test
    void searchWorkouts_WorkoutsFound_ReturnsWorkoutDtoList() {
        String query = "Workout A";

        Workout workout1 = createWorkout();

        WorkoutDto workoutDto1 = WorkoutDto.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00").build();

        when(workoutRepository.findAllByUserAndSearchQuery(user, query))
                .thenReturn(List.of(workout1));
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutMapper.mapToWorkoutDto(workout1)).thenReturn(workoutDto1);

        List<WorkoutDto> result = workoutService.searchWorkouts(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Workout A", result.get(0).getTitle());

        verify(userService, times(1)).getCurrentUser();
        verify(workoutRepository, times(1)).findAllByUserAndSearchQuery(user, query);
        verify(workoutMapper, times(1)).mapToWorkoutDto(workout1);
    }

    @Test
    void addWeekDelimiters_EmptyList() {
        List<WorkoutDto> workouts = new ArrayList<>();

        List<Object> result = workoutService.addWeekDelimiters(workouts);

        assertEquals(0, result.size());
    }

    @Test
    void addWeekDelimiters_SameWeek() {
        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name("Squat").sets(3).reps(5).weight(80.0).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        WorkoutDto workoutDto1 = WorkoutDto.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00")
                .exercises(exerciseDtos).build();
        WorkoutDto workoutDto2 = WorkoutDto.builder()
                .title("Workout B")
                .time("17:00")
                .date(LocalDate.of(2024, 1, 5))
                .exercises(exerciseDtos).build();

        List<WorkoutDto> workouts = List.of(workoutDto1, workoutDto2);

        List<Object> result = workoutService.addWeekDelimiters(workouts);

        assertEquals(3, result.size());
        assertEquals("Week starting on: 2024-01-01", result.get(0));
        assertEquals(workouts.get(0), result.get(1));
        assertEquals(workouts.get(1), result.get(2));
    }

    @Test
    void addWeekDelimiters_MultipleWeeks() {
        ExerciseLogDto logDto = ExerciseLogDto.builder()
                .name("Squat").sets(3).reps(5).weight(80.0).build();
        List<ExerciseLogDto> exerciseDtos = List.of(logDto);

        WorkoutDto workoutDto1 = WorkoutDto.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00")
                .exercises(exerciseDtos).build();
        WorkoutDto workoutDto2 = WorkoutDto.builder()
                .title("Workout B")
                .time("17:00")
                .date(LocalDate.of(2024, 1, 5))
                .exercises(exerciseDtos).build();
        WorkoutDto workoutDto3 = WorkoutDto.builder()
                .title("Workout A")
                .time("17:00")
                .date(LocalDate.of(2024, 1, 8))
                .exercises(exerciseDtos).build();

        List<WorkoutDto> workouts = List.of(workoutDto1, workoutDto2, workoutDto3);

        List<Object> result = workoutService.addWeekDelimiters(workouts);

        assertEquals(5, result.size());
        assertEquals("Week starting on: 2024-01-01", result.get(0));
        assertEquals(workouts.get(0), result.get(1));
        assertEquals(workouts.get(1), result.get(2));
        assertEquals("Week starting on: 2024-01-08", result.get(3));
        assertEquals(workouts.get(2), result.get(4));
    }
}
