package com.hyuseinlesho.powerlog.model;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType type;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Exercise exercise = (Exercise) object;
        return Objects.equals(name, exercise.name) && type == exercise.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
