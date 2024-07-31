package com.hyuseinlesho.powerlog.aop;

import com.hyuseinlesho.powerlog.events.UserRegisteredEvent;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.service.AuthenticationService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserRegistrationAspect {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationAspect.class);

    private final AuthenticationService authenticationService;

    public UserRegistrationAspect(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @AfterReturning("execution(* com.hyuseinlesho.powerlog.events.UserRegisteredEventListener.onApplicationEvent(..)) && args(event)")
    public void logUserRegistration(UserRegisteredEvent event) {
        String username = event.getUsername();
        UserEntity user = authenticationService.getByUsername(username);
        logger.info("User registered with ID: " + user.getId() + ", welcome email sent to: " + user.getEmail());
    }
}
