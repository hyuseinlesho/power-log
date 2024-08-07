package com.hyuseinlesho.powerlog.events;

import com.hyuseinlesho.powerlog.model.dto.Contact;

public record ContactCreatedEvent(Contact contact) {
    @Override
    public Contact contact() {
        return contact;
    }
}
