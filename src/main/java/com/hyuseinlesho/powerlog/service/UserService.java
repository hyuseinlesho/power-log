package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.model.dto.UserProfileDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;

public interface UserService {
    void registerUser(RegisterUserDto registerDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    boolean changeEmail(String newEmail);

    boolean changePassword(String oldPassword, String newPassword);

    UserProfileDto getSessionUser();

    UserEntity getCurrentUser();
}
