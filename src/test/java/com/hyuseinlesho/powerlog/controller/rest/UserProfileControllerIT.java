package com.hyuseinlesho.powerlog.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuseinlesho.powerlog.model.dto.ChangeEmailDto;
import com.hyuseinlesho.powerlog.model.dto.ChangePasswordDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserProfileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserProfileController userProfileController;

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
        userRepository.deleteAll();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(username = "test_user")
    void changeEmail_BlankEmail_ReturnsValidationError() throws Exception {
        ChangeEmailDto emailDto = new ChangeEmailDto();
        emailDto.setNewEmail("");

        mockMvc.perform(post("/users/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emailDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newEmail").value("Email is required"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changeEmail_InvalidEmailFormat_ReturnsValidationError() throws Exception {
        ChangeEmailDto emailDto = new ChangeEmailDto();
        emailDto.setNewEmail("test_user2@");

        mockMvc.perform(post("/users/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emailDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newEmail").value("Invalid email format"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changeEmail_EmaiOver50Characters_ReturnsValidationError() throws Exception {
        ChangeEmailDto emailDto = new ChangeEmailDto();
        emailDto.setNewEmail("test_user_test_user_test_user_test_user25@gmail.com");

        mockMvc.perform(post("/users/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emailDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newEmail").value("Email must be less than 50 characters"));
    }


    @Test
    @WithMockUser(username = "test_user")
    public void changeEmail_Success_ReturnsSuccessMessage() throws Exception {
        ChangeEmailDto emailDto = new ChangeEmailDto();
        emailDto.setNewEmail("test_user2@gmail.com");

        when(userService.changeEmail(emailDto.getNewEmail())).thenReturn(true);

        mockMvc.perform(post("/users/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Email changed successfully."));
    }

    @Test
    @WithMockUser(username = "test_user")
    public void changeEmail_Failure_ReturnsFailureMessage() throws Exception {
        ChangeEmailDto emailDto = new ChangeEmailDto();
        emailDto.setNewEmail("newemail@example.com");

        when(userService.changeEmail(emailDto.getNewEmail())).thenReturn(false);

        mockMvc.perform(post("/users/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to change email."));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changePassword_InvalidInput_ReturnsValidationErrors() throws Exception {
        ChangePasswordDto passwordDto = ChangePasswordDto.builder()
                        .oldPassword("").newPassword(null).confirmPassword("").build();

        mockMvc.perform(post("/users/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.oldPassword").value("Old password is required"))
                .andExpect(jsonPath("$.newPassword").value("New password is required"))
                .andExpect(jsonPath("$.confirmPassword")
                        .value("Password confirmation is required"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changePassword_NewPasswordLessThan8Characters_ReturnsValidationError() throws Exception {
        ChangePasswordDto passwordDto = ChangePasswordDto.builder()
                .oldPassword("test1234").newPassword("test123").confirmPassword("test123").build();

        mockMvc.perform(post("/users/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newPassword")
                        .value("New password must be between 8 and 50 characters"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changePassword_NewPasswordMoreThan8Characters_ReturnsValidationError() throws Exception {
        ChangePasswordDto passwordDto = ChangePasswordDto.builder()
                .oldPassword("test1234")
                .newPassword("test1234test1234test1234test1234test1234test1234test1234")
                .confirmPassword("test1234test1234test1234test1234test1234test1234test1234").build();

        mockMvc.perform(post("/users/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newPassword")
                        .value("New password must be between 8 and 50 characters"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changePassword_NewPasswordAndConfirmationDoNotMatch_ReturnsFailureMessage() throws Exception {
        ChangePasswordDto passwordDto = ChangePasswordDto.builder()
                .oldPassword("test1234").newPassword("test12345").confirmPassword("test1234").build();

        mockMvc.perform(post("/users/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("New password and confirmation do not match"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changePassword_OldPasswordNotCorrect_ReturnsFailureMessage() throws Exception {
        ChangePasswordDto passwordDto = ChangePasswordDto.builder()
                .oldPassword("test4321")
                .newPassword("test12345")
                .confirmPassword("test12345").build();

        when(userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/users/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Old password isn't correct"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void changePassword_Success_ReturnsSuccessMessage() throws Exception {
        ChangePasswordDto passwordDto = ChangePasswordDto.builder()
                .oldPassword("test1234")
                .newPassword("test12345")
                .confirmPassword("test12345").build();

        when(userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword()))
                .thenReturn(true);

        mockMvc.perform(post("/users/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Password changed successfully."));
    }
}
