package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.model.dto.Contact;
import com.hyuseinlesho.powerlog.model.dto.ContactDto;
import com.hyuseinlesho.powerlog.model.dto.UserDto;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;
    private final ContactClient contactClient;

    public AdminRestController(UserService userService, ContactClient contactClient) {
        this.userService = userService;
        this.contactClient = contactClient;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/contacts")
    public List<ContactDto> getAllContacts() {
        return contactClient.getAllContacts().block();
    }
}
