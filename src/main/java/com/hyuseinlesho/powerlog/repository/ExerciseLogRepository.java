package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
}
