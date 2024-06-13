package com.hyuseinlesho.powerlog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int sets;

    @Column(nullable = false)
    private int reps;

    @Column(nullable = false)
    private double weight;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;
}
