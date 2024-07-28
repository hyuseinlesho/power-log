package com.hyuseinlesho.powerlog.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hyuseinlesho.powerlog.mapper.ProgressPhotoMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateProgressPhotoDto;
import com.hyuseinlesho.powerlog.model.dto.ProgressPhotoDto;
import com.hyuseinlesho.powerlog.model.entity.ProgressPhoto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.ProgressPhotoRepository;
import com.hyuseinlesho.powerlog.service.ProgressPhotoService;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProgressPhotoServiceImpl implements ProgressPhotoService {

    private final Cloudinary cloudinary;
    private final ProgressPhotoRepository progressPhotoRepository;
    private final ProgressPhotoMapper progressPhotoMapper;
    private final UserService userService;

    public ProgressPhotoServiceImpl(
            Cloudinary cloudinary,
            ProgressPhotoRepository progressPhotoRepository,
            ProgressPhotoMapper progressPhotoMapper,
            UserService userService
    ) {
        this.cloudinary = cloudinary;
        this.progressPhotoRepository = progressPhotoRepository;
        this.progressPhotoMapper = progressPhotoMapper;
        this.userService = userService;
    }

    @Override
    public List<ProgressPhotoDto> getAllPhotos() {
        List<ProgressPhoto> photos = progressPhotoRepository.findAllByUser(userService.getCurrentUser());
        return photos.stream()
                .map(progressPhotoMapper::mapToProgressPhotoDto)
                .toList();
    }

    @Override
    public void saveProgressPhoto(CreateProgressPhotoDto photoDto) throws IOException {
        MultipartFile photo = photoDto.getPhoto();
        UserEntity user = userService.getCurrentUser();

        Map uploadResult = cloudinary.uploader().upload(photo.getBytes(),
                ObjectUtils.asMap("folder", "uploads/progress-photos/" + user.getUsername()));

        String url = (String) uploadResult.get("url");

        ProgressPhoto progressPhoto = mapToProgressPhoto(photoDto, url, user);

        progressPhotoRepository.save(progressPhoto);
    }

    public ProgressPhoto mapToProgressPhoto(CreateProgressPhotoDto dto, String url, UserEntity user) {
        return ProgressPhoto.builder()
                .date(dto.getDate())
                .filename(dto.getPhoto().getOriginalFilename())
                .url(url)
                .user(user).build();
    }
}
