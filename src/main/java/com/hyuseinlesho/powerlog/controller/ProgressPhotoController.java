package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.exception.PhotoNotFoundException;
import com.hyuseinlesho.powerlog.model.dto.CreateProgressPhotoDto;
import com.hyuseinlesho.powerlog.model.dto.ProgressPhotoDto;
import com.hyuseinlesho.powerlog.service.ProgressPhotoService;
import com.hyuseinlesho.powerlog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/progress-photos")
public class ProgressPhotoController {
    private final ProgressPhotoService progressPhotoService;
    private final UserService userService;

    public ProgressPhotoController(ProgressPhotoService progressPhotoService, UserService userService) {
        this.progressPhotoService = progressPhotoService;
        this.userService = userService;
    }

    @GetMapping
    public String showProgressPhotos(Model model) {
        List<ProgressPhotoDto> progressPhotos = progressPhotoService.getAllPhotos();

        // Group photos by date
        Map<LocalDate, List<ProgressPhotoDto>> groupedPhotos = progressPhotos.stream()
                .collect(Collectors.groupingBy(ProgressPhotoDto::getDate));

        model.addAttribute("groupedPhotos", groupedPhotos);
        model.addAttribute("username", userService.getCurrentUser().getUsername());
        model.addAttribute("photoDto", new CreateProgressPhotoDto());
        return "common/progress-photos";
    }

    @PostMapping("/upload")
    public String uploadProgressPhoto(@Valid CreateProgressPhotoDto photoDto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.photoDto",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("photoDto", photoDto);
            return "redirect:/progress-photos";
        }

        try {
            progressPhotoService.saveProgressPhoto(photoDto);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Photo uploaded successfully."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Failed to upload photo."
            );
        }
        return "redirect:/progress-photos";
    }

    @PostMapping("/{id}/delete")
    public String deletePhoto(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            progressPhotoService.deletePhoto(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Photo deleted successfully!");
        } catch (PhotoNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/progress-photos";
    }
}
