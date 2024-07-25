package com.hyuseinlesho.powerlog.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Scheduled(cron = "* 0 * * * ?")
    public void performDailyTask() {
        System.out.println("Performing daily task at " + new java.util.Date());
    }
}
