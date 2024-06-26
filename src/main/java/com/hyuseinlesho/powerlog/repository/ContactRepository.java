package com.hyuseinlesho.powerlog.repository;

import com.hyuseinlesho.powerlog.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
