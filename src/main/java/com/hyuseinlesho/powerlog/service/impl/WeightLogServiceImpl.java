package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.repository.WeightLogRepository;
import com.hyuseinlesho.powerlog.service.UserService;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightLogServiceImpl implements WeightLogService {
    private final WeightLogRepository weightLogRepository;
    private final UserService userService;

    public WeightLogServiceImpl(WeightLogRepository weightLogRepository, UserService userService) {
        this.weightLogRepository = weightLogRepository;
        this.userService = userService;
    }

    @Override
    public List<WeightLogDto> getWeightLogs() {
        UserEntity currentUser = userService.getCurrentUser();
        List<WeightLog> weightLogs = weightLogRepository.findAllByUser(currentUser);

        return weightLogs.stream()
                .map(WeightLogMapper.INSTANCE::mapToWeightLogDto)
                .toList();
    }

    @Override
    public void addWeightLog(CreateWeightLogDto weightLogDto) {
        UserEntity currentUser = userService.getCurrentUser();

        WeightLog weightLog = WeightLogMapper.INSTANCE.mapToWeightLog(weightLogDto);
        weightLog.setUser(currentUser);

        weightLogRepository.save(weightLog);
    }
}
