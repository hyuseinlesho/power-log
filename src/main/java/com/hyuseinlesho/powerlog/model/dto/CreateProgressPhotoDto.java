package com.hyuseinlesho.powerlog.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class CreateProgressPhotoDto {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Photo file is required")
    private MultipartFile photo;
}
