package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class ContactClient {
    private final RestClient restClient;

    @Value("${contact.service.url}")
    private String contactServiceUrl;

    public ContactClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void saveContact(CreateContactDto contactDto) {
        restClient.post()
                .uri(contactServiceUrl + "/api/contacts/create")
                .contentType(APPLICATION_JSON)
                .body(contactDto)
                .retrieve();
    }

    public List<Contact> getNewContactsSince(LocalDateTime since) {
        String url = String.format("%s/api/contacts/since?since=%s", contactServiceUrl, since);
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
