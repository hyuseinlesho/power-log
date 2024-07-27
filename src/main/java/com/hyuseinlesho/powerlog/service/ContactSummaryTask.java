package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.entity.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContactSummaryTask {

    @Value("${admin.email}")
    private String adminEmail;

    private final ContactClient contactClient;
    private final EmailService emailService;

    public ContactSummaryTask(ContactClient contactClient, EmailService emailService) {
        this.contactClient = contactClient;
        this.emailService = emailService;
    }

    // Every day at 8 AM
//    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 */1 * * * ?")
    public void sendDailyContactSummary() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<Contact> newContacts = contactClient.getNewContactsSince(yesterday);

        if (!newContacts.isEmpty()) {
            StringBuilder emailContent = new StringBuilder("Daily Contact Summary:\n\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Contact contact : newContacts) {
                emailContent.append("Name: ").append(contact.getName()).append("\n")
                        .append("Email: ").append(contact.getEmail()).append("\n")
                        .append("Message: ").append(contact.getMessage()).append("\n")
                        .append("Received At: ").append(contact.getCreatedAt().format(formatter)).append("\n\n");
            }

            emailService.sendEmail(adminEmail, "Daily Contact Summary", emailContent.toString());
        }
    }
}
