package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.ContactDto;
import com.hyuseinlesho.powerlog.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {
    private static final String CONTACT_MESSAGE_SUCCESS = "Thank you for your message! We'll get back to you soon.";
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactDto", new ContactDto());
        return "contact-form";
    }

    @PostMapping("/contact")
    public String submitContactForm(@Valid ContactDto contactDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contactDto", contactDto);
            return "contact-form";
        }

        contactService.saveContact(contactDto);
        model.addAttribute("message", CONTACT_MESSAGE_SUCCESS);
        model.addAttribute("contactDto", new ContactDto());
        return "contact-form";
    }
}
