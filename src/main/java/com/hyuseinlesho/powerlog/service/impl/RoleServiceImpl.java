package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.repository.RoleRepository;
import com.hyuseinlesho.powerlog.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
