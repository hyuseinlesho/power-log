package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.model.entity.Contact;

import java.time.LocalDateTime;
import java.util.List;

public interface ContactService {
    void saveContact(CreateContactDto contactDto);

    List<Contact> getNewContactsSince(LocalDateTime since);
}
