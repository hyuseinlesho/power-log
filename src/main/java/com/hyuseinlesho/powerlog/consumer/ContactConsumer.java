package com.hyuseinlesho.powerlog.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuseinlesho.powerlog.events.ContactCreatedEvent;
import com.hyuseinlesho.powerlog.model.dto.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ContactConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ContactConsumer.class);

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ContactConsumer(ObjectMapper objectMapper, ApplicationEventPublisher eventPublisher) {
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = "contact-topic", groupId = "my-group")
    public void consume(String message) {
        logger.info("Consumed message from contact-topic: " + message);

        Contact contact = parseContactFromString(message);
        processContact(contact);
    }

    private Contact parseContactFromString(String message) {
        try {
            return objectMapper.readValue(message, Contact.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse contact from message: " + message, e);
        }
    }

    private void processContact(Contact contact) {
        // Publish event to send notification email to the admin
//        eventPublisher.publishEvent(new ContactCreatedEvent(contact));
    }
}
