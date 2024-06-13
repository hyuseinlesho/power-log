package com.hyuseinlesho.powerlog.model;

import com.hyuseinlesho.powerlog.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserRole name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    public Role() {
        this.users = new HashSet<>();
    }
}
