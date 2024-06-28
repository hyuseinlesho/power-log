package com.hyuseinlesho.powerlog.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "workouts")
public class Workout extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "workout",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ExerciseLog> exercises;

    private String comment;

    public Workout() {
        this.exercises = new ArrayList<>();
    }
}