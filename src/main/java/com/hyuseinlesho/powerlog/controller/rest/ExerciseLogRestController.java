package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.model.dto.ExerciseLogGraphDto;
import com.hyuseinlesho.powerlog.service.ExerciseLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exercise-logs")
public class ExerciseLogRestController {
    private final ExerciseLogService exerciseLogService;

    public ExerciseLogRestController(ExerciseLogService exerciseLogService) {
        this.exerciseLogService = exerciseLogService;
    }

    @GetMapping
    public ResponseEntity<List<ExerciseLogGraphDto>> getExerciseLogs(
            @RequestParam String exerciseName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ExerciseLogGraphDto> exerciseLogs = exerciseLogService.getExerciseLogs(exerciseName, startDate, endDate);
        return ResponseEntity.ok(exerciseLogs);
    }
}
