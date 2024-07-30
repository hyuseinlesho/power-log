package com.hyuseinlesho.powerlog.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private final String username;

    public UserRegisteredEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
