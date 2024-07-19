package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.LoginUserDto;
import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;

public interface AuthenticationService {
    UserEntity register(RegisterUserDto registerDto);

    UserEntity authenticate(LoginUserDto loginDto);

    UserEntity getByUsername(String username);

    UserEntity getByEmail(String email);
}
