package com.hyuseinlesho.powerlog.events;

import com.hyuseinlesho.powerlog.exception.GlobalExceptionHandler;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.service.AuthenticationService;
import com.hyuseinlesho.powerlog.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventListener implements ApplicationListener<UserRegisteredEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

        String emailBody = "Hi " + username + ",\n\n" +
                "Welcome to PowerLog! We're excited to join you on your strength training journey.\n" +
                "Our mission is to help users track their workouts, monitor progress, and manage their training schedules.\n\n" +
                "Best regards,\n" +
                "The PowerLog Team";
        emailService.sendEmail(user.getEmail(), "Welcome to Our Service", emailBody);
    }
}
