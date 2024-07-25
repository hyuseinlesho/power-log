package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.UserDto;
import com.hyuseinlesho.powerlog.model.dto.UserProfileDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserEntity getByUsername(String username);

    UserEntity getByEmail(String email);

    UserEntity getCurrentUser();

    UserProfileDto getCurrentUserDto();

    boolean changeEmail(String newEmail);

    boolean changePassword(String oldPassword, String newPassword);
}
