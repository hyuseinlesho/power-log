package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.CreateContactDto;
import com.hyuseinlesho.powerlog.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {
    private static final String CONTACT_MESSAGE_SUCCESS = "Thank you for your message! We'll get back to you soon.";

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactDto", new CreateContactDto());
        return "/common/contact";
    }

    @PostMapping("/contact")
    public String submitContactForm(@Valid @ModelAttribute("contactDto") CreateContactDto contactDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "common/contact";
        }

        contactService.saveContact(contactDto);
        redirectAttributes.addFlashAttribute("message", CONTACT_MESSAGE_SUCCESS);
        return "redirect:/contact";
    }
}
