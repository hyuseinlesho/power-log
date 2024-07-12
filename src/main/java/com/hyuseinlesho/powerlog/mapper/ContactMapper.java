package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.model.entity.Contact;
import org.mapstruct.Mapper;

@Mapper
public interface ContactMapper {
    Contact mapToContact(CreateContactDto contactDto);
}
