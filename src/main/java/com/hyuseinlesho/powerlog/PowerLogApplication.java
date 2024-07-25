package com.hyuseinlesho.powerlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PowerLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowerLogApplication.class, args);
    }

}
