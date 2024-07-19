package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.ProgressPhotoDto;
import com.hyuseinlesho.powerlog.model.entity.ProgressPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgressPhotoMapper {
    ProgressPhotoDto mapToProgressPhotoDto(ProgressPhoto progressPhoto);
}
