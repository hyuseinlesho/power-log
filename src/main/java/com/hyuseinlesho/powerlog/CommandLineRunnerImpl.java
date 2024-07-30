package com.hyuseinlesho.powerlog;

import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.service.ContactClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

//@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final ContactClient contactClient;

    public CommandLineRunnerImpl(ContactClient contactClient) {
        this.contactClient = contactClient;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<Contact> newContactsSince = contactClient.getNewContactsSince(yesterday);

        System.out.println();
    }
}
