package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.exception.WeightLogNotFoundException;
import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.*;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.repository.WeightLogRepository;
import com.hyuseinlesho.powerlog.service.impl.WeightLogServiceImpl;
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
public class WeightLogServiceImplTest {

    @Mock
    private WeightLogRepository weightLogRepository;

    @Mock
    private WeightLogMapper weightLogMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private WeightLogServiceImpl weightLogService;

    private UserEntity user;

    public void setUp() {
        user = new UserEntity();
        user.setUsername("test_user");
    }

    private static WeightLog createWeightLog() {
        return WeightLog.builder()
                .weight(70.5)
                .date(LocalDate.of(2024, 1, 1))
                .time("11:00").build();
    }

    @Test
    void createWeightLog_ReturnsWeightLog() {
        CreateWeightLogDto weightLogDto = CreateWeightLogDto.builder()
                .weight(70.2)
                .date(LocalDate.of(2024, 1, 1))
                .time("11:00").build();

        WeightLog weightLog = WeightLog.builder()
                .weight(weightLogDto.getWeight())
                .date(weightLogDto.getDate())
                .time(weightLogDto.getTime()).build();

        UserEntity user = new UserEntity();
        user.setUsername("test_user");

        when(weightLogMapper.mapToWeightLog(weightLogDto)).thenReturn(weightLog);
        when(userService.getCurrentUser()).thenReturn(user);
        when(weightLogRepository.save(weightLog)).thenReturn(weightLog);

        WeightLog createdWeightLog = weightLogService.createWeightLog(weightLogDto);

        assertNotNull(createdWeightLog);
        assertEquals(weightLogDto.getWeight(), createdWeightLog.getWeight());
        assertEquals(weightLogDto.getDate(), createdWeightLog.getDate());
        assertEquals(weightLogDto.getTime(), createdWeightLog.getTime());
        assertEquals(user, createdWeightLog.getUser());
    }

    @Test
    void findAllWeightLogs_NoWeightLogs_ReturnsEmptyList() {
        when(weightLogRepository.findAllByUser(user)).thenReturn(List.of());
        when(userService.getCurrentUser()).thenReturn(user);

        List<WeightLogDto> result = weightLogService.findAllWeightLogs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllWeightLogs_ExercisesExists_ReturnsWeightLogDtoList() {
        WeightLogDto weightLogDto1 = WeightLogDto.builder()
                .weight(70.2)
                .date(LocalDate.of(2024, 1, 1))
                .time("11:00").build();
        WeightLogDto weightLogDto2 = WeightLogDto.builder()
                .weight(70.5)
                .date(LocalDate.of(2024, 1, 2))
                .time("11:00").build();

        WeightLog weightLog1 = WeightLog.builder()
                .weight(weightLogDto1.getWeight())
                .date(weightLogDto1.getDate())
                .time(weightLogDto1.getTime()).build();
        WeightLog weightLog2 = WeightLog.builder()
                .weight(weightLogDto2.getWeight())
                .date(weightLogDto2.getDate())
                .time(weightLogDto2.getTime()).build();

        when(weightLogRepository.findAllByUser(user))
                .thenReturn(List.of(weightLog1, weightLog2));
        when(userService.getCurrentUser()).thenReturn(user);
        when(weightLogMapper.mapToWeightLogDto(weightLog1)).thenReturn(weightLogDto1);
        when(weightLogMapper.mapToWeightLogDto(weightLog2)).thenReturn(weightLogDto2);

        List<WeightLogDto> result = weightLogService.findAllWeightLogs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(70.2, result.get(0).getWeight());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getDate());
        assertEquals("11:00", result.get(0).getTime());
        assertEquals(70.5, result.get(1).getWeight());
        assertEquals(LocalDate.of(2024, 1, 2), result.get(1).getDate());
        assertEquals("11:00", result.get(1).getTime());
    }

    @Test
    void updateWeightLog_WeightLogDoesNotExist_ThrowsWeightLogNotFoundException() {
        Long weightLogId = 1L;

        UpdateWeightLogDto weightLogDto = UpdateWeightLogDto.builder()
                .weight(70.8)
                .date(LocalDate.of(2024, 1, 3))
                .time("11:30").build();

        when(weightLogRepository.findById(weightLogId))
                .thenReturn(Optional.empty());

        assertThrows(WeightLogNotFoundException.class, () -> {
            weightLogService.updateWeightLog(weightLogId, weightLogDto);
        });
        verify(weightLogRepository, never()).save(any(WeightLog.class));
    }

    @Test
    void updateWeightLog_WeightLogExists_ReturnsUpdatedWeightLog() {
        Long weightLogId = 1L;

        UpdateWeightLogDto weightLogDto = UpdateWeightLogDto.builder()
                .weight(70.8)
                .date(LocalDate.of(2024, 1, 2))
                .time("11:30").build();

        WeightLog existingWeightLog = createWeightLog();
        existingWeightLog.setId(weightLogId);

        WeightLog updatedWeightLog = WeightLog.builder()
                .weight(weightLogDto.getWeight())
                .date(weightLogDto.getDate())
                .time(weightLogDto.getTime()).build();
        updatedWeightLog.setId(weightLogId);

        when(weightLogRepository.findById(weightLogId))
                .thenReturn(Optional.of(existingWeightLog));
        when(weightLogRepository.save(existingWeightLog)).thenReturn(updatedWeightLog);

        WeightLog result = weightLogService.updateWeightLog(weightLogId, weightLogDto);

        assertNotNull(result);
        assertEquals(weightLogDto.getWeight(), result.getWeight());
        assertEquals(weightLogDto.getDate(), result.getDate());
        assertEquals(weightLogDto.getTime(), result.getTime());

        verify(weightLogRepository, times(1)).save(existingWeightLog);
    }

    @Test
    void deleteWeightLog_WeightLogDoesNotExist_ThrowsWeightLogNotFoundException() {
        Long weightLogId = 1L;

        when(weightLogRepository.existsById(weightLogId)).thenReturn(false);

        assertThrows(WeightLogNotFoundException.class, () -> {
            weightLogService.deleteWeightLog(weightLogId);
        });

        verify(weightLogRepository, times(1)).existsById(weightLogId);
        verify(weightLogRepository, never()).deleteById(weightLogId);
    }

    @Test
    void deleteWeightLog_WeightLogExists_DeleteById() {
        Long weightLogId = 1L;

        when(weightLogRepository.existsById(weightLogId)).thenReturn(true);
        doNothing().when(weightLogRepository).deleteById(weightLogId);

        weightLogService.deleteWeightLog(weightLogId);

        verify(weightLogRepository, times(1)).existsById(weightLogId);
        verify(weightLogRepository, times(1)).deleteById(weightLogId);
    }

    @Test
    void getWeightLogs_ReturnsWeightLogGraphDtoList() {
        WeightLog weightLog1 = createWeightLog();

        WeightLog weightLog2 = WeightLog.builder()
                .weight(70.8)
                .date(LocalDate.of(2024, 1, 2))
                .time("11:00").build();

        WeightLogGraphDto weightLogGraphDto1 = WeightLogGraphDto.builder()
                .weight(weightLog1.getWeight())
                .date(weightLog1.getDate())
                .time(weightLog1.getTime()).build();

        WeightLogGraphDto weightLogGraphDto2 = WeightLogGraphDto.builder()
                .weight(weightLog2.getWeight())
                .date(weightLog2.getDate())
                .time(weightLog2.getTime()).build();

        List<WeightLog> weightLogs = List.of(weightLog1, weightLog2);

        when(weightLogRepository.findAllByUserOrderByDateAsc(user)).thenReturn(weightLogs);
        when(userService.getCurrentUser()).thenReturn(user);
        when(weightLogMapper.mapToWeightLogGraphDto(weightLog1)).thenReturn(weightLogGraphDto1);
        when(weightLogMapper.mapToWeightLogGraphDto(weightLog2)).thenReturn(weightLogGraphDto2);

        List<WeightLogGraphDto> result = weightLogService.getWeightLogs();

        assertEquals(2, result.size());
        assertEquals(70.5, result.get(0).getWeight());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getDate());
        assertEquals("11:00", result.get(0).getTime());
        assertEquals(70.8, result.get(1).getWeight());
        assertEquals(LocalDate.of(2024, 1, 2), result.get(1).getDate());
        assertEquals("11:00", result.get(1).getTime());

        verify(weightLogRepository, times(1))
                .findAllByUserOrderByDateAsc(user);
        verify(userService, times(1)).getCurrentUser();
        verify(weightLogMapper, times(1)).mapToWeightLogGraphDto(weightLog1);
        verify(weightLogMapper, times(1)).mapToWeightLogGraphDto(weightLog2);
    }

    @Test
    void getWeightLogsBetweenDates_ReturnsWeightLogGraphDtoList() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 5);

        WeightLog weightLog1 = createWeightLog();

        WeightLog weightLog2 = WeightLog.builder()
                .weight(70.8)
                .date(LocalDate.of(2024, 1, 2))
                .time("11:00").build();

        WeightLogGraphDto weightLogGraphDto1 = WeightLogGraphDto.builder()
                .weight(weightLog1.getWeight())
                .date(weightLog1.getDate())
                .time(weightLog1.getTime()).build();

        WeightLogGraphDto weightLogGraphDto2 = WeightLogGraphDto.builder()
                .weight(weightLog2.getWeight())
                .date(weightLog2.getDate())
                .time(weightLog2.getTime()).build();

        List<WeightLog> weightLogs = List.of(weightLog1, weightLog2);

        when(weightLogRepository.findAllByDateBetweenAndUser(startDate, endDate, user))
                .thenReturn(weightLogs);
        when(userService.getCurrentUser()).thenReturn(user);
        when(weightLogMapper.mapToWeightLogGraphDto(weightLog1)).thenReturn(weightLogGraphDto1);
        when(weightLogMapper.mapToWeightLogGraphDto(weightLog2)).thenReturn(weightLogGraphDto2);

        List<WeightLogGraphDto> result = weightLogService.getWeightLogsBetweenDates(startDate, endDate);

        assertEquals(2, result.size());
        assertEquals(70.5, result.get(0).getWeight());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getDate());
        assertEquals("11:00", result.get(0).getTime());
        assertEquals(70.8, result.get(1).getWeight());
        assertEquals(LocalDate.of(2024, 1, 2), result.get(1).getDate());
        assertEquals("11:00", result.get(1).getTime());

        verify(weightLogRepository, times(1))
                .findAllByDateBetweenAndUser(startDate, endDate, user);
        verify(userService, times(1)).getCurrentUser();
        verify(weightLogMapper, times(1)).mapToWeightLogGraphDto(weightLog1);
        verify(weightLogMapper, times(1)).mapToWeightLogGraphDto(weightLog2);
    }
}
