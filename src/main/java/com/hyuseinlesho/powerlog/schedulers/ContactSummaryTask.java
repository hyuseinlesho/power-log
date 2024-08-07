package com.hyuseinlesho.powerlog.schedulers;

import com.hyuseinlesho.powerlog.controller.rest.ContactClient;
import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ContactSummaryTask {
    private static final Logger logger = LoggerFactory.getLogger(ContactSummaryTask.class);

    @Value("${admin.email}")
    private String adminEmail;

    private final ContactClient contactClient;
    private final EmailService emailService;

    public ContactSummaryTask(ContactClient contactClient, EmailService emailService) {
        this.contactClient = contactClient;
        this.emailService = emailService;
    }

    // Test expression for every minute
//    @Scheduled(cron = "0 * * * * ?")

    // Every Monday at 8 AM
    @Scheduled(cron = "0 0 8 * * MON")
    public void sendWeeklyContactSummary() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        Flux<Contact> newContacts = contactClient.getNewContactsSince(lastWeek);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        newContacts.collectList().subscribe(contacts -> {
            if (!contacts.isEmpty()) {
                StringBuilder emailContent = new StringBuilder("Weekly Contact Summary:\n\n");

                for (Contact contact : contacts) {
                    emailContent.append("Name: ").append(contact.getName()).append("\n")
                            .append("Email: ").append(contact.getEmail()).append("\n")
                            .append("Message: ").append(contact.getMessage()).append("\n")
                            .append("Received At: ").append(contact.getCreatedAt().format(formatter)).append("\n\n");
                }

                emailService.sendEmail(adminEmail, "Weekly Contact Summary", emailContent.toString());
                logger.info("Successfully sent weekly contact summary email");
            }
        }, error -> {
            logger.error("Failed to fetch new contacts: " + error.getMessage());
        });
    }
}
