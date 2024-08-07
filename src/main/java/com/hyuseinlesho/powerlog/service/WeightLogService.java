package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogGraphDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;

import java.time.LocalDate;
import java.util.List;

public interface WeightLogService {
    WeightLog createWeightLog(CreateWeightLogDto weightLogDto);

    List<WeightLogDto> getAllWeightLogs();

    WeightLog updateWeightLog(Long id, UpdateWeightLogDto weightLogDto);

    void deleteWeightLog(Long id);

    List<WeightLogGraphDto> getWeightLogGraphDtos();

    List<WeightLogGraphDto> getWeightLogsBetweenDates(LocalDate startDate, LocalDate endDate);
}
