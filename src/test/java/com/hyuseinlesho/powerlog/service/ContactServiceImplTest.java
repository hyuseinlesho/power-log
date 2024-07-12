package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.mapper.ContactMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.model.entity.Contact;
import com.hyuseinlesho.powerlog.repository.ContactRepository;
import com.hyuseinlesho.powerlog.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    public void saveContact() {
        CreateContactDto contactDto = new CreateContactDto();
        contactDto.setName("John Doe");
        contactDto.setEmail("john.doe@example.com");
        contactDto.setMessage("This is a test message.");

        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setEmail(contactDto.getEmail());
        contact.setMessage(contactDto.getMessage());

        when(contactMapper.mapToContact(any(CreateContactDto.class)))
                .thenReturn(contact);
        when(contactRepository.save(any(Contact.class)))
                .thenReturn(contact);

        contactService.saveContact(contactDto);

        verify(contactMapper).mapToContact(eq(contactDto));
        verify(contactRepository).save(eq(contact));
    }
}
