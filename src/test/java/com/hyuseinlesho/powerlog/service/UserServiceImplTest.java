package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.exception.UserNotFoundException;
import com.hyuseinlesho.powerlog.mapper.UserMapper;
import com.hyuseinlesho.powerlog.model.dto.UserDto;
import com.hyuseinlesho.powerlog.model.dto.UserProfileDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private String username;
    private String email;

    @BeforeEach
    public void setUp() {
        username = "test_user";
        email = "test.user@example.com";
    }

    private void setUpSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test_user");
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .username(username)
                .email(email)
                .password("test1234").build();
    }

    @Test
    void getAllUsers_UsersExists_ReturnsUserDtoList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUsers_UsersDoesNotExist_ReturnsEmptyList() {
        UserEntity user1 = createUser();
        UserEntity user2 = UserEntity.builder()
                .username("john_doe")
                .email("john.doe@example.com")
                .password("john1234")
                .build();

        UserDto userDto1 = UserDto.builder()
                .username(user1.getUsername())
                .email(user1.getEmail())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        UserDto userDto2 = UserDto.builder()
                .username("john_doe")
                .email("john.doe@example.com")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        List<UserEntity> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.mapToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.mapToUserDto(user2)).thenReturn(userDto2);

        List<UserDto> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(username, result.get(0).getUsername());
        assertEquals(email, result.get(0).getEmail());
        assertEquals(userDto2.getUsername(), result.get(1).getUsername());
        assertEquals(userDto2.getEmail(), result.get(1).getEmail());
    }

    @Test
    void getByUsername_UserDoesNotExist_ThrowsUserNotFoundException() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getByUsername(username);
        });
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getByUsername_UserExists_ReturnsUser() {
        UserEntity user = createUser();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserEntity result = userService.getByUsername(username);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getByEmail_UserDoesNotExist_ThrowsUserNotFoundException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getByEmail(email);
        });
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getByEmail_UserExists_ReturnsUser() {
        UserEntity user = createUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserEntity result = userService.getByEmail(email);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getCurrentUserDto_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getCurrentUserDto();
        });

        verify(userRepository, times(1)).findByUsername(username);
        verify(userMapper, never()).mapToUserProfileDto(any(UserEntity.class));
    }

    @Test
    void getCurrentUserDto_UserFound_ReturnsUserProfileDto() {
        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email).build();

        UserProfileDto userDto = UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail()).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserProfileDto(user)).thenReturn(userDto);

        UserProfileDto result = userService.getCurrentUserDto();

        assertNotNull(result);
        assertEquals(result.getUsername(), user.getUsername());
        assertEquals(result.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findByUsername(username);
        verify(userMapper, times(1)).mapToUserProfileDto(user);
    }

    @Test
    void changeEmail_UserIsNotFound_ReturnsFalse() {
        setUpSecurityContext();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.changeEmail(email);

        assertFalse(result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void changeEmail_UserIsFound_ChangesEmailAndReturnsTrue() {
        setUpSecurityContext();

        UserEntity user = new UserEntity();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.changeEmail(email);

        assertTrue(result);
        assertEquals(email, user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_UserIsNotFound_ReturnsFalse() {
        setUpSecurityContext();
        String oldPassword = "old_password";
        String newPassword = "new_password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.changePassword(oldPassword, newPassword);

        assertFalse(result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void changePassword_UserIsFound_OldPasswordDoesNotMatch_ReturnsFalse() {
        setUpSecurityContext();
        String oldPassword = "old_password";
        String newPassword = "new_password";

        UserEntity user = UserEntity.builder()
                .username(username)
                .password("encoded_old_password").build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        boolean result = userService.changePassword(oldPassword, newPassword);

        assertFalse(result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void changePassword_UserIsFound_OldPasswordMatches_ChangesPasswordAndReturnsTrue() {
        String oldPassword = "old_password";
        String newPassword = "new_password";

        UserEntity user = UserEntity.builder()
                .username(username)
                .password("encoded_old_password").build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded_new_password");

        boolean result = userService.changePassword(oldPassword, newPassword);

        assertTrue(result);
        assertEquals("encoded_new_password", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}
