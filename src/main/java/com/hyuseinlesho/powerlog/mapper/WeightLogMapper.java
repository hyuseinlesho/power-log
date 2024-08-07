package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogGraphDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogResponseDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WeightLogMapper {
    WeightLog mapToWeightLog(CreateWeightLogDto weightLogDto);

    WeightLogDto mapToWeightLogDto(WeightLog weightLog);

    WeightLogResponseDto mapToWeightLogResponseDto(WeightLog weightLog);

    WeightLogGraphDto mapToWeightLogGraphDto(WeightLog weightLog);
}
