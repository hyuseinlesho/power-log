package com.hyuseinlesho.powerlog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private Date createdAt;
    private Date updatedAt;
}
