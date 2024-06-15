package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.ExerciseLog;
import com.hyuseinlesho.powerlog.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findAllByWorkout(Workout workout);
}
