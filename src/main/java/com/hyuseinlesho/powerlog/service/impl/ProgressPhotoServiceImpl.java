package com.hyuseinlesho.powerlog.service.impl;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProgressPhotoServiceImpl implements ProgressPhotoService {

    //TODO Refactor progress photos upload path

//    @Value("${upload.path}")
    private String uploadPath;

    private final ProgressPhotoRepository progressPhotoRepository;
    private final ProgressPhotoMapper progressPhotoMapper;
    private final UserService userService;

    public ProgressPhotoServiceImpl(
            ProgressPhotoRepository progressPhotoRepository,
            ProgressPhotoMapper progressPhotoMapper,
            UserService userService
    ) {
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

        String filename = UUID.randomUUID() + "-" + photo.getOriginalFilename();
        Path userDirectory = Paths.get(uploadPath, user.getUsername());
        Files.createDirectories(userDirectory);

        Path filePath = userDirectory.resolve(filename);
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ProgressPhoto progressPhoto = new ProgressPhoto();
        progressPhoto.setDate(photoDto.getDate());
        progressPhoto.setFilename(filename);
        progressPhoto.setUser(user);

        progressPhotoRepository.save(progressPhoto);
    }
}
