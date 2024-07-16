package com.hyuseinlesho.powerlog.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseRepository exerciseRepository;

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
        exerciseRepository.deleteAll();
        userRepository.deleteAll();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static CreateExerciseDto createCreateExerciseDto() {
        return CreateExerciseDto.builder()
                .name("Squat")
                .type(ExerciseType.Strength).build();
    }

    private Exercise createExercise() {
        return Exercise.builder()
                .name("Squat")
                .type(ExerciseType.Strength)
                .user(user).build();
    }

    @Test
    @WithMockUser(username = "test_user")
    void createExercise_InvalidInput_ReturnsValidationErrors() throws Exception {
        CreateExerciseDto exerciseDto = CreateExerciseDto.builder()
                .name("").type(null).build();

        mockMvc.perform(post("/api/exercises/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.type").value("Type is required"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void createExercise_ExerciseDoesNotExist_ReturnsCreatedExerciseResponseDto() throws Exception {
        CreateExerciseDto exerciseDto = createCreateExerciseDto();

        mockMvc.perform(post("/api/exercises/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Squat"))
                .andExpect(jsonPath("$.type").value("Strength"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void createExercise_ExerciseAlreadyExists_ReturnsInternalServerError() throws Exception {
        Exercise exercise = createExercise();
        exerciseRepository.save(exercise);

        CreateExerciseDto exerciseDto = createCreateExerciseDto();

        mockMvc.perform(post("/api/exercises/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateExercise_InvalidInput_ReturnsValidationErrors() throws Exception {
        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("").type(null).build();

        mockMvc.perform(put("/api/exercises/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.type").value("Type is required"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateExercise_ValidInput_ReturnsUpdatedExerciseResponseDto() throws Exception {
        Exercise exercise = createExercise();
        exerciseRepository.save(exercise);

        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("Updated Squat")
                .type(ExerciseType.Cardio).build();

        mockMvc.perform(put("/api/exercises/{id}", exercise.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Squat"))
                .andExpect(jsonPath("$.type").value("Cardio"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateExercise_ExerciseDoesNotExist_ReturnsNotFound() throws Exception {
        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("Updated Squat")
                .type(ExerciseType.Cardio).build();

        mockMvc.perform(put("/api/exercises/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateExercise_ExerciseAlreadyExists_ReturnsInternalServerError() throws Exception {
        Exercise exercise1 = createExercise();
        exerciseRepository.save(exercise1);

        Exercise exercise2 = Exercise.builder()
                .name("Deadlift")
                .type(ExerciseType.Strength)
                .user(user).build();
        exerciseRepository.save(exercise2);

        UpdateExerciseDto exerciseDto = UpdateExerciseDto.builder()
                .name("Deadlift")
                .type(ExerciseType.Strength).build();

        mockMvc.perform(put("/api/exercises/{id}", exercise1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exerciseDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "test_user")
    void deleteExercise_ValidId_ReturnsNoContent() throws Exception {
        Exercise exercise = createExercise();
        exerciseRepository.save(exercise);

        mockMvc.perform(delete("/api/exercises/{id}", exercise.getId()))
                .andExpect(status().isNoContent());

        Optional<Exercise> deletedExercise = exerciseRepository.findById(exercise.getId());
        assertFalse(deletedExercise.isPresent());
    }
}
