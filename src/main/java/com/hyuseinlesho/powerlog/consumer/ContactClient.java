package com.hyuseinlesho.powerlog.consumer;

import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.model.dto.CreateContactDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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

    public Flux<Contact> getNewContactsSince(LocalDateTime since) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/since")
                        .queryParam("since", since.toString())
                        .build())
                .retrieve()
                .bodyToFlux(Contact.class);
    }
}
