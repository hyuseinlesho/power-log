package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findAllByUserOrderByDateAsc(UserEntity user);

    @Query("SELECT w FROM Workout w WHERE w.user = :user AND (w.title LIKE %:query% OR CAST(w.date AS string) LIKE %:query% OR w.time LIKE %:query%)")
    List<Workout> findByUserAndSearchQuery(UserEntity user, String query);
}
