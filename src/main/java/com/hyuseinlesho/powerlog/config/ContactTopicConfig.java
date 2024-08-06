package com.hyuseinlesho.powerlog.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ContactTopicConfig {

    @Bean
    public NewTopic contactTopic() {
        return TopicBuilder
                .name("contact-topic")
                .build();
    }
}
