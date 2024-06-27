package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.model.UserEntity;

public interface UserService {
    void registerUser(RegisterUserDto registerDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
