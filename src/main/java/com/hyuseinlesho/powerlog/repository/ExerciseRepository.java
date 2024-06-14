package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByUserUsername(String username);
    boolean existsByName(String name);
}
