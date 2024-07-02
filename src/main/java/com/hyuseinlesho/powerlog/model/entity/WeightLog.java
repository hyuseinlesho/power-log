package com.hyuseinlesho.powerlog.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "weight_logs")
public class WeightLog extends BaseEntity {

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String time;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}

