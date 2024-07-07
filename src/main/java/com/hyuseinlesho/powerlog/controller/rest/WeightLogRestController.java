package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.*;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weight-logs")
public class WeightLogRestController {
    private final WeightLogService weightLogService;

    public WeightLogRestController(WeightLogService weightLogService) {
        this.weightLogService = weightLogService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWeightLog(@Valid @RequestBody CreateWeightLogDto weightLogDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = ControllerUtil.validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        WeightLog created = weightLogService.createWeightLog(weightLogDto);
        WeightLogResponseDto response = WeightLogMapper.INSTANCE.mapToWeightLogResponseDto(created);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWeightLog(@PathVariable Long id,
                                             @Valid @RequestBody UpdateWeightLogDto weightLogDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = ControllerUtil.validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        WeightLog updated = weightLogService.updateWeightLog(id, weightLogDto);
        WeightLogResponseDto response = WeightLogMapper.INSTANCE.mapToWeightLogResponseDto(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWeightLog(@PathVariable Long id) {
        weightLogService.deleteWeightLog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<WeightLogGraphDto>> getWeightLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<WeightLogGraphDto> weightLogs;
        if (startDate != null && endDate != null) {
            weightLogs = weightLogService.getWeightLogsBetweenDates(startDate, endDate);
        } else {
            weightLogs = weightLogService.getWeightLogs();
        }
        return ResponseEntity.ok(weightLogs);
    }
}
