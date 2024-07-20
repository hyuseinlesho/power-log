package com.hyuseinlesho.powerlog.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "routine_exercises")
public class RoutineExercise extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private Integer sets;
    private Integer reps;

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;
}
