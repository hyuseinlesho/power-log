package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.dto.ContactDto;
import com.hyuseinlesho.powerlog.model.Contact;

public interface ContactService {
    Contact saveContact(ContactDto contactDto);
}
