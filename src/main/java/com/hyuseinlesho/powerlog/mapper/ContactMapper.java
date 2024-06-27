package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {
    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    Contact mapToContact(CreateContactDto contactDto);
}
