package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.UserEntity;

public interface UserService {
    UserEntity findByUsername(String username);
}
