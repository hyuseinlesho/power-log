package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;

public interface ContactService {
    void saveContact(CreateContactDto contactDto);
}
