package com.hyuseinlesho.powerlog.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String email;
    private Date createdAt;
    private Date updatedAt;
}
