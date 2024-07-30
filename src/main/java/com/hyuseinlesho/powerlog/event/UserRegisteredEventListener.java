package com.hyuseinlesho.powerlog.event;

import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.service.AuthenticationService;
import com.hyuseinlesho.powerlog.service.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventListener implements ApplicationListener<UserRegisteredEvent> {
    private final EmailService emailService;
    private final AuthenticationService authenticationService;

    public UserRegisteredEventListener(EmailService emailService, AuthenticationService authenticationService) {
        this.emailService = emailService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        String username = event.getUsername();
        UserEntity user = authenticationService.getByUsername(username);

        String emailBody = "Welcome " + user.getUsername() + ", thank you for registering to PowerLog application!";
        emailService.sendEmail(user.getEmail(), "Welcome to Our Service", emailBody);

        System.out.println("User registered with ID: " + user.getId() + ", welcome email sent to: " + user.getEmail());
    }
}
