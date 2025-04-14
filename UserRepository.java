package com.bookshop.repository;

import com.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username (used by Spring Security login process)
    Optional<User> findByUsername(String username);

    //  Check if a username already exists (for validation)
    boolean existsByUsername(String username);
}
