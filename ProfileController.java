package com.bookshop.controller;

import com.bookshop.entity.User;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Show profile form
    @GetMapping
    public String showProfileForm(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    // Handle profile update
    @PostMapping
    public String updateProfile(@ModelAttribute("user") User updatedUser, Principal principal, Model model) {
        User existingUser = userRepository.findByUsername(principal.getName()).orElseThrow();

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setShippingAddress(updatedUser.getShippingAddress());
        existingUser.setCardNumber(updatedUser.getCardNumber());
        existingUser.setCardType(updatedUser.getCardType());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userService.updateUserProfile(existingUser);
        model.addAttribute("user", existingUser);
        model.addAttribute("message", "Profile updated successfully!");

        return "profile";
    }
}

