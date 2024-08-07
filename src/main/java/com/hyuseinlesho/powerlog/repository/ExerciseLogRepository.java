package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findAllByWorkout(Workout workout);

    @Query("SELECT el " +
            "FROM ExerciseLog el " +
            "WHERE el.name = :exerciseName " +
            "AND el.workout.date BETWEEN :startDate AND :endDate " +
            "AND el.workout.user = :user")
    List<ExerciseLog> findAllByExerciseNameAndDateRange(
            @Param("exerciseName") String exerciseName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("user") UserEntity user
            );
}
