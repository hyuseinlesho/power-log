package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWeightLogDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.repository.WeightLogRepository;
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
import java.util.Optional;

import static com.hyuseinlesho.powerlog.config.ApplicationBeanConfiguration.objectMapper;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class WeightLogRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeightLogRepository weightLogRepository;

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
        weightLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    private static String asJsonString(final Object obj) {
        try {
            return objectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static CreateWeightLogDto createCreateWeightLogDto() {
        return CreateWeightLogDto.builder()
                .weight(70.5)
                .date(LocalDate.of(2024, 1, 1))
                .time("11:00").build();
    }

    private WeightLog createWeightLog() {
        return WeightLog.builder()
                .weight(70.5)
                .date(LocalDate.of(2024, 1, 1))
                .time("11:00")
                .user(user).build();
    }

    @Test
    @WithMockUser(username = "test_user")
    void createWeightLog_NullInputValues_ReturnsValidationErrors() throws Exception {
        CreateWeightLogDto weightLogDto = CreateWeightLogDto.builder()
                .weight(null).date(null).time("").build();

        mockMvc.perform(post("/api/weight-logs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(weightLogDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weight").value("Weight is required"))
                .andExpect(jsonPath("$.date").value("Date is required"))
                .andExpect(jsonPath("$.time").value("Time is required"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void createWeightLog_CommentLongerThan200CharactersInput_ReturnsValidationError() throws Exception {
        String longComment = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget" +
                "dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur" +
                "ridiculus mus. Donec quam felis, ultricies.";

        CreateWeightLogDto weightLogDto = createCreateWeightLogDto();
        weightLogDto.setComment(longComment);

        mockMvc.perform(post("/api/weight-logs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(weightLogDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.comment")
                        .value("Comment cannot be longer than 200 characters"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void createWeightLog_ExerciseDoesNotExist_ReturnsCreatedWeightLogResponseDto() throws Exception {
        CreateWeightLogDto weightLogDto = createCreateWeightLogDto();

        mockMvc.perform(post("/api/weight-logs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(weightLogDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.weight").value(70.5))
                .andExpect(jsonPath("$.date").value("2024-01-01"))
                .andExpect(jsonPath("$.time").value("11:00"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateWeightLog_NullInputValues_ReturnsValidationErrors() throws Exception {
        WeightLog weightLog = createWeightLog();
        weightLogRepository.save(weightLog);

        UpdateWeightLogDto weightLogDto = UpdateWeightLogDto.builder()
                .weight(null).date(null).time("").build();

        mockMvc.perform(put("/api/weight-logs/{id}", weightLog.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(weightLogDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weight").value("Weight is required"))
                .andExpect(jsonPath("$.date").value("Date is required"))
                .andExpect(jsonPath("$.time").value("Time is required"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateWeightLog_CommentLongerThan200CharactersInput_ReturnsValidationError() throws Exception {
        String longComment = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget" +
                "dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur" +
                "ridiculus mus. Donec quam felis, ultricies.";

        WeightLog weightLog = createWeightLog();
        weightLog.setComment("Test comment");
        weightLogRepository.save(weightLog);

        UpdateWeightLogDto weightLogDto = UpdateWeightLogDto.builder()
                .weight(weightLog.getWeight())
                .date(weightLog.getDate())
                .time(weightLog.getTime())
                .comment(longComment).build();

        mockMvc.perform(put("/api/weight-logs/{id}", weightLog.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(weightLogDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.comment")
                        .value("Comment cannot be longer than 200 characters"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void updateWeightLog_ValidInput_ReturnsUpdatedWeightLogResponseDto() throws Exception {
        WeightLog weightLog = createWeightLog();
        weightLog.setComment("Test comment");
        weightLogRepository.save(weightLog);

        UpdateWeightLogDto weightLogDto = UpdateWeightLogDto.builder()
                .weight(70.8)
                .date(LocalDate.of(2024, 1, 2))
                .time("10:30")
                .comment("Updated test comment").build();

        mockMvc.perform(put("/api/weight-logs/{id}", weightLog.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(weightLogDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(weightLogDto.getWeight()))
                .andExpect(jsonPath("$.date").value(weightLogDto.getDate().toString()))
                .andExpect(jsonPath("$.time").value(weightLogDto.getTime()))
                .andExpect(jsonPath("$.comment").value(weightLogDto.getComment()));
    }

    @Test
    @WithMockUser(username = "test_user")
    void deleteWeightLog_ValidId_ReturnsNoContent() throws Exception {
        WeightLog weightLog = createWeightLog();
        weightLogRepository.save(weightLog);

        mockMvc.perform(delete("/api/weight-logs/{id}", weightLog.getId()))
                .andExpect(status().isNoContent());

        Optional<WeightLog> deletedWeightLog = weightLogRepository.findById(weightLog.getId());
        assertFalse(deletedWeightLog.isPresent());
    }

    @Test
    @WithMockUser(username = "test_user")
    void getWeightLogs_BetweenSpecificDates_ReturnsFilteredWeightLogs() throws Exception {
        WeightLog weightLog1 = createWeightLog();

        WeightLog weightLog2 = WeightLog.builder()
                .weight(71.0)
                .date(LocalDate.of(2024, 2, 1))
                .time("12:00")
                .user(user).build();

        weightLogRepository.save(weightLog1);
        weightLogRepository.save(weightLog2);

        mockMvc.perform(get("/api/weight-logs")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].weight").value(70.5));
    }

    @Test
    @WithMockUser(username = "test_user")
    void getWeightLogs_ReturnsAllWeightLogs() throws Exception {
        WeightLog weightLog1 = createWeightLog();

        WeightLog weightLog2 = WeightLog.builder()
                .weight(71.0)
                .date(LocalDate.of(2024, 2, 1))
                .time("12:00")
                .user(user).build();

        weightLogRepository.save(weightLog1);
        weightLogRepository.save(weightLog2);

        mockMvc.perform(get("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].weight").value(70.5))
                .andExpect(jsonPath("$[1].weight").value(71.0));
    }

    @Test
    @WithMockUser(username = "test_user")
    void getWeightLogs_NoWeightLogsExist_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "test_user")
    void getWeightLogs_StartDateAfterEndDate_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/weight-logs")
                        .param("startDate", "2024-02-01")
                        .param("endDate", "2024-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
