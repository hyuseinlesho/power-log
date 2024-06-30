package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/add")
    public String addWeightLog(@Valid @ModelAttribute CreateWeightLogDto weightLogDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<WeightLogDto> weightLogs = weightLogService.getWeightLogs();
            redirectAttributes.addFlashAttribute("weightLogs", weightLogs);
            return "redirect:/weight-logs";
        }

        weightLogService.addWeightLog(weightLogDto);
        return "redirect:/weight-logs";
    }
}
