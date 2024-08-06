package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.model.dto.ContactDto;
import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactClient {
    private final WebClient webClient;

    public ContactClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8081/api/contacts")
                .build();
    }

    public Mono<Contact> saveContact(CreateContactDto contactDto) {
        return webClient.post()
                .uri("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDto)
                .retrieve()
                .bodyToMono(Contact.class);
    }

    public Mono<List<ContactDto>> getAllContacts() {
        Flux<Contact> contactFlux = webClient.get()
                .retrieve()
                .bodyToFlux(Contact.class);

        return contactFlux.collectList()
                .map(contacts -> contacts.stream()
                        .map(ContactClient::mapToContactDto)
                        .toList());
    }

    public Flux<Contact> getNewContactsSince(LocalDateTime since) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/since")
                        .queryParam("since", since.toString())
                        .build())
                .retrieve()
                .bodyToFlux(Contact.class);
    }

    private static ContactDto mapToContactDto(Contact contact) {
        return ContactDto.builder()
                .name(contact.getName())
                .email(contact.getEmail())
                .message(contact.getMessage())
                .createdAt(contact.getCreatedAt())
                .build();
    }
}
