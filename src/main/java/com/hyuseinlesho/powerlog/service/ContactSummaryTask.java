package com.hyuseinlesho.powerlog.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContactSummaryTask {
//    private static final String ADMIN_EMAIL = "";
//
//    private final ContactService contactService;
//    private final EmailService emailService;
//
//    public ContactSummaryTask(ContactService contactService, EmailService emailService) {
//        this.contactService = contactService;
//        this.emailService = emailService;
//    }
//
//    // Every day at 8 AM
//    @Scheduled(cron = "0 0 8 * * ?")
//    public void sendDailyContactSummary() {
//        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
//        List<Contact> newContacts = contactService.getNewContactsSince(yesterday);
//
//        if (!newContacts.isEmpty()) {
//            StringBuilder emailContent = new StringBuilder("Daily Contact Summary:\n\n");
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//            for (Contact contact : newContacts) {
//                emailContent.append("Name: ").append(contact.getName()).append("\n")
//                        .append("Email: ").append(contact.getEmail()).append("\n")
//                        .append("Message: ").append(contact.getMessage()).append("\n")
//                        .append("Received At: ").append(contact.getCreatedAt().format(formatter)).append("\n\n");
//            }
//
//            emailService.sendEmail(ADMIN_EMAIL, "Daily Contact Summary", emailContent.toString());
//        }
//    }
}
