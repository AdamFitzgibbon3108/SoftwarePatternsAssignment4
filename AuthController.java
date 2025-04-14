package com.bookshop.controller;

import com.bookshop.entity.User;
import com.bookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }

        // Encrypt password and assign default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER");

        userRepository.save(user);

        // Redirect to login page after successful registration
        return "redirect:/login?success";
    }

    // Show custom login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Optional home page (not used if redirected to dashboard after login)
    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    // New: Dashboard page after login
    @GetMapping("/dashboard")
    public String dashboardPage(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "dashboard";
    }
}
