package com.bookshop.repository;

import com.bookshop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

    List<Review> findByBookIdAndUserId(Long bookId, Long userId);
}
