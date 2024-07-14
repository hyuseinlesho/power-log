package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.mapper.ExerciseLogMapper;
import com.hyuseinlesho.powerlog.model.dto.ExerciseLogGraphDto;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.service.impl.ExerciseLogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseLogServiceImplTest {

    @Mock
    private ExerciseLogRepository exerciseLogRepository;

    @Mock
    private ExerciseLogMapper exerciseLogMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExerciseLogServiceImpl exerciseLogService;

    @Test
    void getExerciseLogs_ExerciseLogsNotFound_ReturnsEmptyList() {
        String exerciseName = "Squat";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseLogRepository.findExerciseLogsByExerciseNameAndDateRange(exerciseName, startDate, endDate, user))
                .thenReturn(Collections.emptyList());
        when(userService.getCurrentUser()).thenReturn(user);

        List<ExerciseLogGraphDto> result = exerciseLogService.getExerciseLogs(exerciseName, startDate, endDate);

        assertEquals(result, List.of());

        verify(exerciseLogRepository, times(1))
                .findExerciseLogsByExerciseNameAndDateRange(exerciseName, startDate, endDate, user);
        verify(userService, times(1)).getCurrentUser();
        verify(exerciseLogMapper, never()).mapToExerciseLogGraphDto(any(ExerciseLog.class));
    }

    @Test
    void getExerciseLogs_ExerciseLogsFound_ReturnsExerciseLogGraphDtoList() {
        String exerciseName = "Squat";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);

        ExerciseLog exerciseLog1 = ExerciseLog.builder()
                .name("Squat").sets(3).reps(5).weight(80).build();
        ExerciseLog exerciseLog2 = ExerciseLog.builder()
                .name("Squat").sets(3).reps(5).weight(82.5).build();

        ExerciseLogGraphDto logDto1 = ExerciseLogGraphDto.builder()
                .exerciseName(exerciseLog1.getName())
                .sets(exerciseLog1.getSets())
                .reps(exerciseLog1.getReps())
                .weight(exerciseLog1.getWeight()).build();
        ExerciseLogGraphDto logDto2 = ExerciseLogGraphDto.builder()
                .exerciseName(exerciseLog2.getName())
                .sets(exerciseLog2.getSets())
                .reps(exerciseLog2.getReps())
                .weight(exerciseLog2.getWeight()).build();

        List<ExerciseLog> exercises = List.of(exerciseLog1, exerciseLog2);

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(exerciseLogRepository.findExerciseLogsByExerciseNameAndDateRange(exerciseName, startDate, endDate, user))
                .thenReturn(exercises);
        when(userService.getCurrentUser()).thenReturn(user);
        when(exerciseLogMapper.mapToExerciseLogGraphDto(exerciseLog1)).thenReturn(logDto1);
        when(exerciseLogMapper.mapToExerciseLogGraphDto(exerciseLog2)).thenReturn(logDto2);

        List<ExerciseLogGraphDto> result = exerciseLogService.getExerciseLogs(exerciseName, startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(logDto1, result.get(0));
        assertEquals(logDto2, result.get(1));

        verify(exerciseLogRepository, times(1))
                .findExerciseLogsByExerciseNameAndDateRange(exerciseName, startDate, endDate, user);
        verify(userService, times(1)).getCurrentUser();
        verify(exerciseLogMapper, times(1)).mapToExerciseLogGraphDto(exerciseLog1);
        verify(exerciseLogMapper, times(1)).mapToExerciseLogGraphDto(exerciseLog2);
    }
}
