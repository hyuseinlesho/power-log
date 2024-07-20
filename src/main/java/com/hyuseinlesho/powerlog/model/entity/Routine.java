package com.hyuseinlesho.powerlog.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "routines")
public class Routine extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "routine",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RoutineExercise> exercises;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public Routine() {
        this.exercises = new ArrayList<>();
    }
}
