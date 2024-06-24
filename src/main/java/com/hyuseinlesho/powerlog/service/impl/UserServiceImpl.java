package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.dto.UserRegisterDto;
import com.hyuseinlesho.powerlog.mapper.UserMapper;
import com.hyuseinlesho.powerlog.model.Role;
import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.model.enums.UserRole;
import com.hyuseinlesho.powerlog.repository.RoleRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements com.hyuseinlesho.powerlog.service.UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void registerUser(UserRegisterDto registerDto) {
        UserEntity user = UserMapper.INSTANCE.mapToUserEntity(registerDto);

        Role role = roleRepository.findByName(UserRole.USER);
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
}
