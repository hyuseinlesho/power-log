package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.CreateContactDto;

public interface ContactService {
    void saveContact(CreateContactDto contactDto);
}
