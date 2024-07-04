package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogResponseDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
}
