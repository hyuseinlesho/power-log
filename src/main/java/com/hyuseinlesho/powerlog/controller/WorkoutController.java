package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.mapper.ExerciseLogMapper;
import com.hyuseinlesho.powerlog.model.dto.*;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.RoutineService;
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
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final RoutineService routineService;
    private final ExerciseLogMapper exerciseLogMapper;

    public WorkoutController(
            WorkoutService workoutService,
            ExerciseService exerciseService,
            RoutineService routineService,
            ExerciseLogMapper exerciseLogMapper) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
        this.routineService = routineService;
        this.exerciseLogMapper = exerciseLogMapper;
    }

    @GetMapping("/history")
    public String showWorkoutHistory(Model model) {
        List<WorkoutDto> workouts = workoutService.getAllWorkoutsSortedByDate();
        List<Object> workoutsWithDelimiters = workoutService.addWeekDelimiters(workouts);
        model.addAttribute("workoutsWithDelimiters", workoutsWithDelimiters);
        return "/workouts/history";
    }

    @GetMapping("/create")
    public String showCreateWorkoutForm(Model model) {
        model.addAttribute("workoutDto", new CreateWorkoutDto());
        model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
        model.addAttribute("exerciseTypes", ExerciseType.values());
        model.addAttribute("routines", routineService.getRoutines());
        return "/workouts/create";
    }

    @PostMapping("/create")
    public String handleCreateWorkout(
            @RequestParam(value = "exerciseCount", required = false) Integer exerciseCount,
            @RequestParam(value = "routineId", required = false) Long routineId,
            @Valid @ModelAttribute("workoutDto") CreateWorkoutDto workoutDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (exerciseCount != null) {
            List<ExerciseLogDto> exercises = new ArrayList<>();

            for (int i = 0; i < exerciseCount; i++) {
                exercises.add(new ExerciseLogDto());
            }

            workoutDto.setExercises(exercises);
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "workouts/create";
        }

        if (routineId != null) {
            RoutineDto routine = routineService.getRoutineById(routineId);

            // TODO Fix title not showing after select routine
            workoutDto.setTitle(routine.getName());

            List<ExerciseLogDto> exercises = routine.getExercises()
                    .stream()
                    .map(exerciseLogMapper::mapToExerciseLogDto)
                    .toList();

            workoutDto.setExercises(exercises);
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "workouts/create";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("routines", routineService.getRoutines());
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "workouts/create";
        }

        workoutService.createWorkout(workoutDto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Workout created successfully!");
        return "redirect:/workouts/history";
    }

    @GetMapping("/{id}/edit")
    public String showEditWorkoutForm(@PathVariable("id") Long id,
                                      Model model) {
        WorkoutDto workout = workoutService.getWorkoutById(id);
        model.addAttribute("workoutDto", workout);
        model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
        model.addAttribute("exerciseTypes", ExerciseType.values());
        return "/workouts/edit";
    }

    @PostMapping("/{id}/edit")
    public String editWorkout(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("workoutDto") UpdateWorkoutDto workoutDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        workoutDto.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "/workouts/edit";
        }

        workoutService.editWorkout(workoutDto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Workout updated successfully!");
        return "redirect:/workouts/{id}/details";
    }

    @GetMapping("/{id}/details")
    public String getWorkoutDetails(@PathVariable("id") Long id, Model model) {
        WorkoutDto workout = workoutService.getWorkoutById(id);
        model.addAttribute("workoutDto", workout);
        return "/workouts/details";
    }

    @PostMapping("/{id}/delete")
    public String deleteWorkout(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        workoutService.deleteWorkout(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Workout deleted successfully!");
        return "redirect:/workouts/history";
    }

    @GetMapping("/history/search")
    public String searchWorkouts(@RequestParam("query") String query,
                                 Model model) {
        List<WorkoutDto> workouts = workoutService.searchWorkouts(query);
        model.addAttribute("workouts", workouts);
        return "/workouts/history";
    }
}
