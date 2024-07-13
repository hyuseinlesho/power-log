package com.hyuseinlesho.powerlog.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDto {
    private Long id;
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String time;
    private List<ExerciseLogDto> exercises;
    private double totalVolume;
    private String comment;

    public String getDayOfWeek() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }
}
