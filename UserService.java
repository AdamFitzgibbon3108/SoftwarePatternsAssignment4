package com.bookshop.service;

import com.bookshop.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void updateUserProfile(User user);
}
