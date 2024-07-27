package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.model.dto.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactClient {
    private final RestTemplate restTemplate;

    @Value("${contact.service.url}")
    private String contactServiceUrl;

    public ContactClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void saveContact(CreateContactDto contactDto) {
        restTemplate.postForEntity(contactServiceUrl + "/api/contacts", contactDto, Void.class);
    }

    public List<Contact> getNewContactsSince(LocalDateTime since) {
        String url = String.format("%s/api/contacts/since?since=%s", contactServiceUrl, since);
        ResponseEntity<List<Contact>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
