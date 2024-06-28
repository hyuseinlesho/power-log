package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.CreateExerciseLogDto;
import com.hyuseinlesho.powerlog.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    public static final String CREATE_SUCCESS_MESSAGE = "Workout created successfully!";
    public static final String UPDATE_SUCCESS_MESSAGE = "Workout updated successfully!";
    public static final String DELETE_SUCCESS_MESSAGE = "Workout deleted successfully!";

    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    public WorkoutController(WorkoutService workoutService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/history")
    public String showWorkoutHistory(Model model) {
        List<Workout> workouts = workoutService.findAllWorkouts();
        model.addAttribute("workouts", workouts);
        return "/workouts/history";
    }

    @GetMapping("/create")
    public String showCreateWorkoutForm(Model model) {
        model.addAttribute("workoutDto", new CreateWorkoutDto());
        model.addAttribute("exerciseOptions", exerciseService.findAllExercises());
        return "/workouts/create";
    }

    @PostMapping("/create")
    public String createWorkout(@RequestParam(required = false) Integer exerciseCount,
                                @Valid @ModelAttribute("workoutDto") CreateWorkoutDto workoutDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (exerciseCount != null) {
            List<CreateExerciseLogDto> exercises = new ArrayList<>();
            for (int i = 0; i < exerciseCount; i++) {
                exercises.add(new CreateExerciseLogDto());
            }
            workoutDto.setExercises(exercises);
            model.addAttribute("exerciseOptions", exerciseService.findAllExercises());
            return "/workouts/create";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseOptions", exerciseService.findAllExercises());
            return "/workouts/create";
        }

        workoutService.createWorkout(workoutDto);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_SUCCESS_MESSAGE);
        return "redirect:/workouts/history";
    }

    @GetMapping("/{id}/edit")
    public String showEditWorkoutForm(@PathVariable("id") Long id,
                                      Model model) {
        CreateWorkoutDto workoutDto = workoutService.findWorkoutById(id);
        model.addAttribute("workoutDto", workoutDto);
        model.addAttribute("exerciseOptions", exerciseService.findAllExercises());
        return "/workouts/edit";
    }

    @PostMapping("/{id}/edit")
    public String editWorkout(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("workoutDto") CreateWorkoutDto workoutDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        workoutDto.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseOptions", exerciseService.findAllExercises());
            return "/workouts/edit";
        }

        workoutService.editWorkout(workoutDto);
        redirectAttributes.addFlashAttribute("successMessage", UPDATE_SUCCESS_MESSAGE);
        return "redirect:/workouts/{id}/details";
    }

    @GetMapping("/{id}/details")
    public String getWorkoutDetails(@PathVariable("id") Long id,
                                    Model model) {
        CreateWorkoutDto workoutDto = workoutService.findWorkoutById(id);
        model.addAttribute("workoutDto", workoutDto);
        return "/workouts/details";
    }

    @PostMapping("/{id}/delete")
    public String deleteWorkout(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        workoutService.deleteWorkout(id);
        redirectAttributes.addFlashAttribute("successMessage", DELETE_SUCCESS_MESSAGE);
        return "redirect:/workouts/history";
    }

    @GetMapping("/history/search")
    public String searchWorkouts(@RequestParam("query") String query,
                                 Model model) {
        List<CreateWorkoutDto> workouts = workoutService.searchWorkouts(query);
        model.addAttribute("workouts", workouts);
        return "/workouts/history";
    }
}
