package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByUser(UserEntity currentUser);

    Optional<Exercise> findByNameAndTypeAndUser(String name, ExerciseType type, UserEntity user);

    @Query("SELECT e FROM Exercise e WHERE e.user = :user AND e.name = :name AND e.id <> :id")
    List<Exercise> findByNameAndUserAndIdNot(UserEntity user, String name, Long id);
}
