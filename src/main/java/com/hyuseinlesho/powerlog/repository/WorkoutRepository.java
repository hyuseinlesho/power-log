package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findAllByUser(UserEntity user);

    @Query("SELECT w FROM Workout w WHERE w.user.username = :username AND (w.title LIKE %:query% OR CAST(w.dateTime AS string) LIKE %:query%)")
    List<Workout> findByUserAndSearchQuery(String username, String query);
}
