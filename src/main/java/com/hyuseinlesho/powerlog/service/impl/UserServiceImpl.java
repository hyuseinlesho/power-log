package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
