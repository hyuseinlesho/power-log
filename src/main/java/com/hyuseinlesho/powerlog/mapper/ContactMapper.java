package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.ContactDto;
import com.hyuseinlesho.powerlog.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {
    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    Contact contactDtoToContact(ContactDto contactDto);

    ContactDto contactToContactDto(Contact contact);
}
