package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.ProgressPhoto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressPhotoRepository extends JpaRepository<ProgressPhoto, Long> {
    List<ProgressPhoto> findAllByUser(UserEntity currentUser);
}
