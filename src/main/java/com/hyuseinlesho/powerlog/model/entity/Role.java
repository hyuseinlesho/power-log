package com.hyuseinlesho.powerlog.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles",
            fetch = FetchType.EAGER)
    private Set<UserEntity> users;

    public Role() {
        this.users = new HashSet<>();
    }
}
