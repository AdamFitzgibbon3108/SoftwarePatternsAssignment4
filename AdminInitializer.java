package com.bookshop.bootstrap;

import com.bookshop.entity.User;
import com.bookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        userRepository.findByUsername("admin").ifPresentOrElse(
            user -> System.out.println(">>> Admin user already exists."),
            () -> {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                admin.setTotalSpent(0.0);
                userRepository.save(admin);
                System.out.println(">>> Admin user created with username 'admin' and password 'admin123'");
            }
        );
    }
}

