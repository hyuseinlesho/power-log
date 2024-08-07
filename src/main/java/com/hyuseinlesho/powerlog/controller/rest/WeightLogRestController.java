package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogGraphDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogResponseDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weight-logs")
public class WeightLogRestController {
    private final WeightLogService weightLogService;
    private final WeightLogMapper weightLogMapper;

    public WeightLogRestController(WeightLogService weightLogService, WeightLogMapper weightLogMapper) {
        this.weightLogService = weightLogService;
        this.weightLogMapper = weightLogMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWeightLog(@Valid @RequestBody CreateWeightLogDto weightLogDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = ControllerUtil.validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        WeightLog created = weightLogService.createWeightLog(weightLogDto);
        WeightLogResponseDto response = weightLogMapper.mapToWeightLogResponseDto(created);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
        WeightLogResponseDto response = weightLogMapper.mapToWeightLogResponseDto(updated);

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
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        List<WeightLogGraphDto> weightLogs;
        if (startDate != null && endDate != null) {
            weightLogs = weightLogService.getWeightLogsBetweenDates(startDate, endDate);
        } else {
            weightLogs = weightLogService.getWeightLogGraphDtos();
        }
        return ResponseEntity.ok(weightLogs);
    }
}
