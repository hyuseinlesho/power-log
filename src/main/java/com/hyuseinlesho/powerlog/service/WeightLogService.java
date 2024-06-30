package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;

import java.util.List;

public interface WeightLogService {
    List<WeightLogDto> getWeightLogs();
    void addWeightLog(CreateWeightLogDto weightLogDto);
}
