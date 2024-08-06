package com.hyuseinlesho.powerlog.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDto {
    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
}
