package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE c.createdAt >= :since")
    List<Contact> findContactsSince(@Param("since") LocalDateTime since);
}
