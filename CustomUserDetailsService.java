package com.bookshop.service;

import com.bookshop.entity.User;
import com.bookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">> Attempting to load user: " + username);

        User user = userRepository.findByUsername(username)
                   .orElseThrow(() -> {
                       System.out.println(">> User not found in database: " + username);
                       return new UsernameNotFoundException("User not found: " + username);
                   });

        System.out.println(">> User found: " + user.getUsername());
        System.out.println(">> Role: " + user.getRole());
        System.out.println(">> Encrypted password from DB: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
