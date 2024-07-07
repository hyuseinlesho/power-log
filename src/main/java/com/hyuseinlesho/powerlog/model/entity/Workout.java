package com.hyuseinlesho.powerlog.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@Entity
@Table(name = "workouts")
public class Workout extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "workout",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ExerciseLog> exercises;

    @Column(name = "total_volume")
    private Double totalVolume;

    private String comment;

    public Workout() {
        this.exercises = new ArrayList<>();
    }
}
