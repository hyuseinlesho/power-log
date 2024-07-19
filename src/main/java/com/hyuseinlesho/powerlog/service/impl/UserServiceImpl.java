package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.exception.UserNotFoundException;
import com.hyuseinlesho.powerlog.mapper.UserMapper;
import com.hyuseinlesho.powerlog.model.dto.UserProfileDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.security.SecurityUtil;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public boolean changeEmail(String newEmail) {
        try {
            UserEntity user = getCurrentUser();
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        try {
            UserEntity user = getCurrentUser();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        } catch (UserNotFoundException e) {
            // TODO Log the exception or handle it as necessary
        }
        return false;
    }

    @Override
    public UserProfileDto getCurrentUserDto() {
        UserEntity user = userRepository.findByUsername(SecurityUtil.getSessionUser())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.mapToUserProfileDto(user);
    }

    @Override
    public UserEntity getCurrentUser() {
        String username = SecurityUtil.getSessionUser();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
