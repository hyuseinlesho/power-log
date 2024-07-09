package com.hyuseinlesho.powerlog.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Workout> workouts;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    private Set<Exercise> exercises;

    @OneToMany(mappedBy = "user")
    private List<WeightLog> weightLogs;

    @OneToMany(mappedBy = "user")
    private List<ProgressPhoto> progressPhotos;

    public UserEntity() {
        this.roles = new HashSet<>();
        this.workouts = new HashSet<>();
        this.exercises = new HashSet<>();
    }
}
