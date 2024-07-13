package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.exception.ExerciseNotFoundException;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @Test
    public void createExercise_ExerciseExists_ThrowsExerciseAlreadyExistsException() {
        CreateExerciseDto exerciseDto = CreateExerciseDto.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();

        Exercise exercise = Exercise.builder()
                .name(exerciseDto.getName())
                .type(exerciseDto.getType()).build();

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        List<Exercise> existingExercises = List.of(exercise);

        when(exerciseMapper.mapToExercise(exerciseDto)).thenReturn(exercise);
        when(exerciseRepository.findAllByUser(user)).thenReturn(existingExercises);
        when(userService.getCurrentUser()).thenReturn(user);

        assertThrows(ExerciseAlreadyExistsException.class, () -> {
            exerciseService.createExercise(exerciseDto);
        });
        verify(exerciseRepository, never()).save(exercise);
    }

    @Test
    public void createExercise_ExerciseDoesNotExist_ReturnsExercise() {
        CreateExerciseDto exerciseDto = CreateExerciseDto.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();

        Exercise exercise = Exercise.builder()
                .name(exerciseDto.getName())
                .type(exerciseDto.getType()).build();

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseMapper.mapToExercise(exerciseDto)).thenReturn(exercise);
        when(exerciseRepository.findAllByUser(user))
                .thenReturn(Collections.emptyList());
        when(userService.getCurrentUser()).thenReturn(user);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        Exercise createdExercise = exerciseService.createExercise(exerciseDto);

        assertNotNull(createdExercise);
        assertEquals(exerciseDto.getName(), createdExercise.getName());
        assertEquals(exerciseDto.getType(), createdExercise.getType());
        assertEquals(user, createdExercise.getUser());

        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    public void addNewExercise_ExerciseDoesNotExist_ReturnsTrue() {
        String name = "Squat";
        ExerciseType type = ExerciseType.Strength;

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseRepository.findByNameAndTypeAndUser(name, type, user))
                .thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(user);

        boolean result = exerciseService.addNewExercise(name, type);

        assertTrue(result);
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    public void addNewExercise_ExerciseExists_ReturnsFalse() {
        String name = "Squat";
        ExerciseType type = ExerciseType.Strength;

        Exercise existingExercise = Exercise.builder()
                .name(name)
                .type(type).build();

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(userService.getCurrentUser()).thenReturn(user);
        when(exerciseRepository.findByNameAndTypeAndUser(name, type, user))
                .thenReturn(Optional.of(existingExercise));

        boolean result = exerciseService.addNewExercise(name, type);

        assertFalse(result);
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void findExerciseById_ExerciseDoesNotExist_ThrowsExerciseNotFoundException() {
        Long exerciseId = 1L;

        Exercise exercise = Exercise.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> {
            exerciseService.findExerciseById(exerciseId);
        });
        verify(exerciseMapper, never()).mapToExerciseDto(exercise);
    }

    @Test
    public void findExerciseById_ExerciseExists_ReturnsCreateExerciseDto() {
        Long exerciseId = 1L;

        ExerciseDto exerciseDto = ExerciseDto.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();

        Exercise exercise = Exercise.builder()
                .name(exerciseDto.getName())
                .type(exerciseDto.getType()).build();
        exercise.setId(exerciseId);

        when(exerciseRepository.findById(exerciseId))
                .thenReturn(Optional.of(exercise));
        when(exerciseMapper.mapToExerciseDto(exercise))
                .thenReturn(exerciseDto);

        ExerciseDto foundExercise = exerciseService.findExerciseById(exerciseId);

        assertNotNull(foundExercise);
        assertEquals(exerciseDto.getName(), foundExercise.getName());
        assertEquals(exerciseDto.getType(), foundExercise.getType());
    }

    @Test
    public void findAllExercises_NoExercises_ReturnsEmptyList() {
        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseRepository.findAllByUser(user)).thenReturn(List.of());
        when(userService.getCurrentUser()).thenReturn(user);

        List<ExerciseDto> result = exerciseService.findAllExercises();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllExercises_ExercisesExist_ReturnsExerciseDtoList() {
        ExerciseDto exerciseDto1 = ExerciseDto.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();

        ExerciseDto exerciseDto2 = ExerciseDto.builder()
                .name("Running")
                .type(ExerciseType.Cardio).build();

        Exercise exercise1 = Exercise.builder()
                .name(exerciseDto1.getName())
                .type(exerciseDto1.getType()).build();

        Exercise exercise2 = Exercise.builder()
                .name(exerciseDto2.getName())
                .type(exerciseDto2.getType()).build();

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        List<Exercise> exercises = List.of(exercise1, exercise2);

        when(exerciseRepository.findAllByUser(user)).thenReturn(exercises);
        when(userService.getCurrentUser()).thenReturn(user);

        List<ExerciseDto> result = exerciseService.findAllExercises();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Squat", result.get(0).getName());
        assertEquals(ExerciseType.Strength, result.get(0).getType());
        assertEquals("Running", result.get(1).getName());
        assertEquals(ExerciseType.Cardio, result.get(1).getType());
    }

    @Test
    public void updateExercise_ExerciseDoesNotExist_ThrowsExerciseNotFoundException() {
        Long exerciseId = 1L;

        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("Updated Squat")
                .type(ExerciseType.Strength).build();

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> {
            exerciseService.updateExercise(exerciseId, exerciseDto);
        });
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void updateExercise_ExerciseAlreadyExists_ThrowsExerciseAlreadyExistsException() {
        Long exerciseId = 1L;

        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("Updated Squat")
                .type(ExerciseType.Strength).build();

        Exercise existingExercise = Exercise.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();
        existingExercise.setId(exerciseId);

        Exercise duplicateExercise = Exercise.builder()
                .name(exerciseDto.getName())
                .type(exerciseDto.getType()).build();
        duplicateExercise.setId(2L);

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.findAllByUser(user))
                .thenReturn(List.of(existingExercise, duplicateExercise));
        when(userService.getCurrentUser()).thenReturn(user);

        assertThrows(ExerciseAlreadyExistsException.class, () -> {
            exerciseService.updateExercise(exerciseId, exerciseDto);
        });
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void updateExercise_ExerciseExists_ReturnsUpdatedExercise() {
        Long exerciseId = 1L;

        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("Updated Squat")
                .type(ExerciseType.Strength).build();

        Exercise existingExercise = Exercise.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();
        existingExercise.setId(exerciseId);

        Exercise updatedExercise = Exercise.builder()
                .name(exerciseDto.getName())
                .type(exerciseDto.getType()).build();
        updatedExercise.setId(exerciseId);

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseRepository.findById(exerciseId))
                .thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.findAllByUser(user))
                .thenReturn(List.of());
        when(userService.getCurrentUser()).thenReturn(user);
        when(exerciseRepository.save(existingExercise)).thenReturn(updatedExercise);

        Exercise result = exerciseService.updateExercise(exerciseId, exerciseDto);

        verify(exerciseRepository, times(1)).save(existingExercise);
        assertNotNull(result);
        assertEquals(exerciseDto.getName(), result.getName());
        assertEquals(exerciseDto.getType(), result.getType());
    }

    @Test
    public void deleteExercise_ExerciseNotExists_ThrowsExerciseNotFoundException() {
        Long nonExistingExerciseId = 2L;

        when(exerciseRepository.existsById(nonExistingExerciseId)).thenReturn(false);

        assertThrows(ExerciseNotFoundException.class, () -> {
            exerciseService.deleteExercise(nonExistingExerciseId);
        });

        verify(exerciseRepository, times(1)).existsById(nonExistingExerciseId);
        verify(exerciseRepository, never()).deleteById(nonExistingExerciseId);
    }

    @Test
    public void deleteExercise_ExerciseExists_DeleteExercise() {
        Long exerciseId = 1L;

        when(exerciseRepository.existsById(exerciseId)).thenReturn(true);
        doNothing().when(exerciseRepository).deleteById(exerciseId);

        exerciseService.deleteExercise(exerciseId);

        verify(exerciseRepository, times(1)).existsById(exerciseId);
        verify(exerciseRepository, times(1)).deleteById(exerciseId);
    }
}
