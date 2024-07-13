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

import static org.mockito.Mockito.*;

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
        CreateContactDto contactDto = CreateContactDto.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .message("This is a test message.").build();

        Contact contact = Contact.builder()
                .name(contactDto.getName())
                .email(contactDto.getEmail())
                .message(contactDto.getMessage()).build();

        when(contactMapper.mapToContact(contactDto))
                .thenReturn(contact);
        when(contactRepository.save(contact))
                .thenReturn(contact);

        contactService.saveContact(contactDto);

        verify(contactMapper, times(1)).mapToContact(contactDto);
        verify(contactRepository, times(1)).save(contact);
    }
}
