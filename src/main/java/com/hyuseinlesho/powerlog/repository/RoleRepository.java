package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.Role;
import com.hyuseinlesho.powerlog.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(UserRole name);
}
