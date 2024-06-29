package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.model.dto.UserDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;

public interface UserService {
    void registerUser(RegisterUserDto registerDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
    UserDto getSessionUser();

    boolean changeUsername(String newUsername);

    boolean changeEmail(String newEmail);

    boolean changePassword(String newPassword);
}
