package com.hyuseinlesho.powerlog.events;

import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ContactCreatedEventListener{
    private static final Logger logger = LoggerFactory.getLogger(ContactCreatedEventListener.class);

    @Value("${admin.email}")
    private String adminEmail;

    private final EmailService emailService;
    public ContactCreatedEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleContactCreatedEvent(ContactCreatedEvent event) {
        Contact contact = event.contact();

        StringBuilder emailContent = new StringBuilder("New Contact Notification:\n\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        emailContent.append("Name: ").append(contact.getName()).append("\n")
                .append("Email: ").append(contact.getEmail()).append("\n")
                .append("Message: ").append(contact.getMessage()).append("\n")
                .append("Received At: ").append(contact.getCreatedAt().format(formatter)).append("\n\n");

        emailService.sendEmail(adminEmail, "New Contact Notification", emailContent.toString());
        logger.info("Successfully sent notification email for new contact from: " + contact.getName());
    }
}
