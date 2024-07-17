package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.ExerciseLogGraphDto;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseLogService {
    List<ExerciseLogGraphDto> getExerciseLogsBetweenDates(String exerciseName, LocalDate startDate, LocalDate endDate);
}
