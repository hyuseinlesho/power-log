package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.ProgressPhotoDto;
import com.hyuseinlesho.powerlog.model.entity.ProgressPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgressPhotoMapper {
    ProgressPhotoMapper INSTANCE = Mappers.getMapper(ProgressPhotoMapper.class);

    ProgressPhotoDto mapToProgressPhotoDto(ProgressPhoto progressPhoto);
}
