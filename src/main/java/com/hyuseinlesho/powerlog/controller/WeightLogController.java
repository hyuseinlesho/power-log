package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/weight-logs")
public class WeightLogController {
    private final WeightLogService weightLogService;

    public WeightLogController(WeightLogService weightLogService) {
        this.weightLogService = weightLogService;
    }

    @GetMapping
    public String showWeightLogsPage(Model model) {
        List<WeightLogDto> weightLogs = weightLogService.getWeightLogs();
        model.addAttribute("weightLogs", weightLogs);
        return "/weight-logs/list";
    }
}
