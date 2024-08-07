package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.Routine;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByUser(UserEntity user);
}
