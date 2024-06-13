package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements com.hyuseinlesho.powerlog.service.UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
