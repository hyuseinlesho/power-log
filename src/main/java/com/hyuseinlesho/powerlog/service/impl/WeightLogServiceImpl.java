package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.exception.WeightLogNotFoundException;
import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogGraphDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.repository.WeightLogRepository;
import com.hyuseinlesho.powerlog.service.UserService;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeightLogServiceImpl implements WeightLogService {
    private final WeightLogRepository weightLogRepository;
    private final WeightLogMapper weightLogMapper;
    private final UserService userService;

    public WeightLogServiceImpl(WeightLogRepository weightLogRepository, WeightLogMapper weightLogMapper, UserService userService) {
        this.weightLogRepository = weightLogRepository;
        this.weightLogMapper = weightLogMapper;
        this.userService = userService;
    }

    @Override
    public WeightLog createWeightLog(CreateWeightLogDto weightLogDto) {
        WeightLog weightLog = weightLogMapper.mapToWeightLog(weightLogDto);
        weightLog.setUser(userService.getCurrentUser());

        return weightLogRepository.save(weightLog);
    }

    @Override
    public List<WeightLogDto> getAllWeightLogs() {
        List<WeightLog> weightLogs = weightLogRepository.findAllByUser(userService.getCurrentUser());

        return weightLogs.stream()
                .map(weightLogMapper::mapToWeightLogDto)
                .toList();
    }

    @Override
    public WeightLog updateWeightLog(Long id, UpdateWeightLogDto weightLogDto) {
        WeightLog weightLog = weightLogRepository.findById(id)
                .orElseThrow(() -> new WeightLogNotFoundException("WeightLog not found for id: " + id));

        weightLog.setWeight(weightLogDto.getWeight());
        weightLog.setDate(weightLogDto.getDate());
        weightLog.setTime(weightLogDto.getTime());
        weightLog.setComment(weightLogDto.getComment());

        return weightLogRepository.save(weightLog);
    }

    @Override
    public void deleteWeightLog(Long id) {
        if (!weightLogRepository.existsById(id)) {
            throw new WeightLogNotFoundException("WeightLog not found for id: " + id);
        }

        weightLogRepository.deleteById(id);
    }

    @Override
    public List<WeightLogGraphDto> getWeightLogs() {
        return weightLogRepository.findAllByUserOrderByDateAsc(userService.getCurrentUser())
                .stream()
                .map(weightLogMapper::mapToWeightLogGraphDto)
                .toList();
    }

    @Override
    public List<WeightLogGraphDto> getWeightLogsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return weightLogRepository.findAllByDateBetweenAndUser(startDate, endDate, userService.getCurrentUser())
                .stream()
                .map(weightLogMapper::mapToWeightLogGraphDto)
                .toList();
    }
}
