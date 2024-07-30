package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.event.UserRegisteredEvent;
import com.hyuseinlesho.powerlog.model.dto.LoginUserDto;
import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.mapper.UserMapper;
import com.hyuseinlesho.powerlog.model.entity.Role;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.repository.RoleRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.service.AuthenticationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserEntity register(RegisterUserDto registerDto) {
        UserEntity user = userMapper.mapToUserEntity(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepository.findByName("USER");
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(this, user.getUsername());
        eventPublisher.publishEvent(event);

        return user;
    }

    @Override
    public UserEntity authenticate(LoginUserDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        return userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow();
    }

    @Override
    public UserEntity getByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public UserEntity getByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}
