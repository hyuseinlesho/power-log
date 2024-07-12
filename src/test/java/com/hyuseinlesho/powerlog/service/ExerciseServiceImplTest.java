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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public void setUpSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        "test_user", "test1234"
                ));
    }

    @Test
    public void createExercise_ExerciseDoesNotExist_ReturnsExercise() {
        setUpSecurityContext();

        CreateExerciseDto exerciseDto = new CreateExerciseDto();
        exerciseDto.setName("Squat");
        exerciseDto.setType(ExerciseType.Strength);

        Exercise exercise = new Exercise();
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseMapper.mapToExercise(exerciseDto)).thenReturn(exercise);
        when(exerciseRepository.findAllByUserUsername("test_user"))
                .thenReturn(Collections.emptyList());
        when(userService.getCurrentUser()).thenReturn(user);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        Exercise createdExercise = exerciseService.createExercise(exerciseDto);

        verify(exerciseRepository, times(1)).save(exercise);
        assertNotNull(createdExercise);
        assertEquals(exerciseDto.getName(), createdExercise.getName());
        assertEquals(exerciseDto.getType(), createdExercise.getType());
        assertEquals(user, createdExercise.getUser());
    }

    @Test
    public void createExercise_ExerciseExists_ThrowsExerciseAlreadyExistsException() {
        setUpSecurityContext();

        CreateExerciseDto exerciseDto = new CreateExerciseDto();
        exerciseDto.setName("Squat");
        exerciseDto.setType(ExerciseType.Strength);

        Exercise exercise = new Exercise();
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());

        List<Exercise> existingExercises = List.of(exercise);

        when(exerciseMapper.mapToExercise(exerciseDto)).thenReturn(exercise);
        when(exerciseRepository.findAllByUserUsername("test_user")).thenReturn(existingExercises);

        assertThrows(ExerciseAlreadyExistsException.class, () -> {
            exerciseService.createExercise(exerciseDto);
        });
        verify(exerciseRepository, never()).save(exercise);
    }

    @Test
    public void addNewExercise_ExerciseDoesNotExist_ReturnsTrue() {
        String name = "Squat";
        ExerciseType type = ExerciseType.Strength;

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseRepository.findByNameAndType(name, type))
                .thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(user);

        boolean result = exerciseService.addNewExercise(name, type);

        verify(exerciseRepository, times(1)).save(any(Exercise.class));
        assertTrue(result);
    }

    @Test
    public void addNewExercise_ExerciseExists_ReturnsFalse() {
        String name = "Squat";
        ExerciseType type = ExerciseType.Strength;

        Exercise existingExercise = new Exercise();
        existingExercise.setName(name);
        existingExercise.setType(type);

        when(exerciseRepository.findByNameAndType(name, type))
                .thenReturn(Optional.of(existingExercise));

        boolean result = exerciseService.addNewExercise(name, type);

        verify(exerciseRepository, times(0)).save(any(Exercise.class));
        assertFalse(result);
    }

    @Test
    public void findExerciseById_ExerciseExists_ReturnsCreateExerciseDto() {
        Long exerciseId = 1L;

        CreateExerciseDto exerciseDto = new CreateExerciseDto();
        exerciseDto.setName("Squat");
        exerciseDto.setType(ExerciseType.Strength);

        Exercise exercise = new Exercise();
        exercise.setId(exerciseId);
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());

        when(exerciseRepository.findById(exerciseId))
                .thenReturn(Optional.of(exercise));
        when(exerciseMapper.mapToCreateExerciseDto(exercise))
                .thenReturn(exerciseDto);

        CreateExerciseDto foundExercise = exerciseService.findExerciseById(exerciseId);

        assertNotNull(foundExercise);
        assertEquals(exerciseDto.getName(), foundExercise.getName());
        assertEquals(exerciseDto.getType(), foundExercise.getType());
    }

    @Test
    public void findExerciseById_ExerciseDoesNotExist_ThrowsExerciseNotFoundException() {
        Long exerciseId = 1L;

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> {
            exerciseService.findExerciseById(exerciseId);
        });
    }

    @Test
    public void findAllExercises_ExercisesExist_ReturnsExerciseDtoList() {
        setUpSecurityContext();

        ExerciseDto exerciseDto1 = new ExerciseDto();
        exerciseDto1.setName("Squat");
        exerciseDto1.setType(ExerciseType.Strength);

        ExerciseDto exerciseDto2 = new ExerciseDto();
        exerciseDto2.setName("Running");
        exerciseDto2.setType(ExerciseType.Cardio);

        Exercise exercise1 = new Exercise();
        exercise1.setName(exerciseDto1.getName());
        exercise1.setType(exerciseDto1.getType());

        Exercise exercise2 = new Exercise();
        exercise2.setName(exerciseDto2.getName());
        exercise2.setType(exerciseDto2.getType());

        List<Exercise> exercises = List.of(exercise1, exercise2);

        when(exerciseRepository.findAllByUserUsername("test_user")).thenReturn(exercises);

        List<ExerciseDto> result = exerciseService.findAllExercises();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Squat", result.get(0).getName());
        assertEquals(ExerciseType.Strength, result.get(0).getType());
        assertEquals("Running", result.get(1).getName());
        assertEquals(ExerciseType.Cardio, result.get(1).getType());
    }

    @Test
    public void findAllExercises_NoExercises_ReturnsEmptyList() {
        setUpSecurityContext();

        when(exerciseRepository.findAllByUserUsername("test_user")).thenReturn(List.of());

        List<ExerciseDto> result = exerciseService.findAllExercises();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void updateExercise_ExerciseDoesNotExist_ThrowsExerciseNotFoundException() {
        Long exerciseId = 1L;

        UpdateExerciseDto exerciseDto = new UpdateExerciseDto();
        exerciseDto.setName("Updated Squat");
        exerciseDto.setType(ExerciseType.Strength);

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> {
            exerciseService.updateExercise(exerciseId, exerciseDto);
        });

        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void updateExercise_ExerciseAlreadyExists_ThrowsExerciseAlreadyExistsException() {
        setUpSecurityContext();

        Long exerciseId = 1L;
        UpdateExerciseDto exerciseDto = new UpdateExerciseDto();
        exerciseDto.setName("Updated Squat");
        exerciseDto.setType(ExerciseType.Strength);

        Exercise existingExercise = new Exercise();
        existingExercise.setId(exerciseId);
        existingExercise.setName("Squat");
        existingExercise.setType(ExerciseType.Strength);

        Exercise duplicateExercise = new Exercise();
        duplicateExercise.setId(2L);
        duplicateExercise.setName(exerciseDto.getName());
        duplicateExercise.setType(exerciseDto.getType());

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.findAllByUserUsername("test_user"))
                .thenReturn(List.of(existingExercise, duplicateExercise));

        assertThrows(ExerciseAlreadyExistsException.class, () -> {
            exerciseService.updateExercise(exerciseId, exerciseDto);
        });

        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void updateExercise_ExerciseExists_ReturnsUpdatedExercise() {
        setUpSecurityContext();

        Long exerciseId = 1L;

        UpdateExerciseDto exerciseDto = new UpdateExerciseDto();
        exerciseDto.setName("Updated Squat");
        exerciseDto.setType(ExerciseType.Strength);

        Exercise existingExercise = new Exercise();
        existingExercise.setId(exerciseId);
        existingExercise.setName("Squat");
        existingExercise.setType(ExerciseType.Strength);

        Exercise updatedExercise = new Exercise();
        updatedExercise.setId(exerciseId);
        updatedExercise.setName(exerciseDto.getName());
        updatedExercise.setType(exerciseDto.getType());

        when(exerciseRepository.findById(exerciseId))
                .thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.findAllByUserUsername("test_user"))
                .thenReturn(List.of());
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
    public void deleteExercise_ExerciseExists_DeletesExercise() {
        Long exerciseId = 1L;

        when(exerciseRepository.existsById(exerciseId)).thenReturn(true);
        doNothing().when(exerciseRepository).deleteById(exerciseId);

        exerciseService.deleteExercise(exerciseId);

        verify(exerciseRepository, times(1)).existsById(exerciseId);
        verify(exerciseRepository, times(1)).deleteById(exerciseId);
    }
}
