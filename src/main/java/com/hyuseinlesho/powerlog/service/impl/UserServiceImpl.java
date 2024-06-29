package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.mapper.UserMapper;
import com.hyuseinlesho.powerlog.model.dto.UserDto;
import com.hyuseinlesho.powerlog.model.entity.Role;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.RoleRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.security.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements com.hyuseinlesho.powerlog.service.UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterUserDto registerDto) {
        UserEntity user = UserMapper.INSTANCE.mapToUserEntity(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepository.findByName("USER");
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto getSessionUser() {
        UserEntity user = userRepository.findByUsername(SecurityUtil.getSessionUser());
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @Override
    public boolean changeUsername(String newUsername) {
        UserEntity user = getCurrentUser();

        if (user != null) {
            user.setUsername(newUsername);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean changeEmail(String newEmail) {
        UserEntity user = getCurrentUser();

        if (user != null) {
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean changePassword(String newPassword) {
        UserEntity user = getCurrentUser();

        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    private UserEntity getCurrentUser() {
        return userRepository.findByUsername(SecurityUtil.getSessionUser());
    }
}
