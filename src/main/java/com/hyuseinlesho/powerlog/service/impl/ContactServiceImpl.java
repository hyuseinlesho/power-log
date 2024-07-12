package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.mapper.ContactMapper;
import com.hyuseinlesho.powerlog.model.entity.Contact;
import com.hyuseinlesho.powerlog.repository.ContactRepository;
import com.hyuseinlesho.powerlog.service.ContactService;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @Override
    public void saveContact(CreateContactDto contactDto) {
        Contact contact = contactMapper.mapToContact(contactDto);
        contactRepository.save(contact);
    }
}
