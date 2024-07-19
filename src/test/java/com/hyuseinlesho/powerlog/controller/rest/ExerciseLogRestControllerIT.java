package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import com.hyuseinlesho.powerlog.repository.ExerciseLogRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseLogRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(context).apply(springSecurity()).build();

        user = UserEntity.builder()
                .username("test_user")
                .email("test_user@gmail.com")
                .password("test1234").build();
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        workoutRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test_user")
    void getExerciseLogs_StartDateAfterEndDate_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/exercise-logs")
                        .param("startDate", "2024-02-01")
                        .param("endDate", "2024-01-01")
                        .param("exerciseName", "Squat")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test_user")
    void getExerciseLogs_BetweenSpecificDatesForGivenExercise_ReturnsFilteredExerciseLogs() throws Exception {
        Workout workout1 = Workout.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 1, 1))
                .time("17:00")
                .user(user)
                .build();
        Workout workout2 = Workout.builder()
                .title("Workout A")
                .date(LocalDate.of(2024, 2, 1))
                .time("17:00")
                .user(user)
                .build();

        ExerciseLog exerciseLog1 = ExerciseLog.builder()
                .name("Squat").sets(3).reps(5).weight(80).workout(workout1).build();
        ExerciseLog exerciseLog2 = ExerciseLog.builder()
                .name("Squat").sets(3).reps(5).weight(90).workout(workout2).build();

        workout1.setExercises(List.of(exerciseLog1));
        workout2.setExercises(List.of(exerciseLog2));

        workoutRepository.save(workout1);
        workoutRepository.save(workout2);

        exerciseLogRepository.save(exerciseLog1);
        exerciseLogRepository.save(exerciseLog2);

        mockMvc.perform(get("/api/exercise-logs")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31")
                        .param("exerciseName", "Squat")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].weight").value(80));
    }
}
