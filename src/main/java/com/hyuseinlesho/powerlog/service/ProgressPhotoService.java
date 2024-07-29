package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateProgressPhotoDto;
import com.hyuseinlesho.powerlog.model.dto.ProgressPhotoDto;

import java.io.IOException;
import java.util.List;

public interface ProgressPhotoService {
    List<ProgressPhotoDto> getAllPhotos();

    void saveProgressPhoto(CreateProgressPhotoDto photoDto) throws IOException;

    void deletePhoto(Long photoId);
}
