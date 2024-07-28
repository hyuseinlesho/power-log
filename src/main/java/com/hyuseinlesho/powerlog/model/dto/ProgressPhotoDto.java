package com.hyuseinlesho.powerlog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProgressPhotoDto {
    private LocalDate date;
    private String filename;
    private String url;
}
