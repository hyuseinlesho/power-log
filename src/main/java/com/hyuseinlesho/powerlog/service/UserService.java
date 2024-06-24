package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.UserRegisterDto;
import com.hyuseinlesho.powerlog.model.UserEntity;

public interface UserService {
    void registerUser(UserRegisterDto registerDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
