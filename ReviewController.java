package com.bookshop.controller;

import com.bookshop.entity.Book;
import com.bookshop.entity.Review;
import com.bookshop.entity.User;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.ReviewRepository;
import com.bookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // Show book detail with reviews
    @GetMapping("/book/{bookId}")
    public String showBookWithReviews(@PathVariable Long bookId, Model model, Principal principal) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        List<Review> reviews = reviewRepository.findByBookId(bookId);

        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);

        boolean alreadyReviewed = false;
        boolean canReview = false;

        if (principal != null) {
            Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Review> existingReviews = reviewRepository.findByBookIdAndUserId(bookId, user.getId());
                alreadyReviewed = !existingReviews.isEmpty();
                canReview = existingReviews.isEmpty();
            }
        }

        model.addAttribute("alreadyReviewed", alreadyReviewed);
        model.addAttribute("canReview", canReview);
        model.addAttribute("newReview", new Review());

        return "book-detail";
    }

    // Submit a new review
    @PostMapping("/book/{bookId}")
    public String submitReview(@PathVariable Long bookId,
                               @ModelAttribute("newReview") Review review,
                               Principal principal) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        List<Review> existingReviews = reviewRepository.findByBookIdAndUserId(bookId, user.getId());
        if (!existingReviews.isEmpty()) {
            return "redirect:/reviews/book/" + bookId; // Skip if already reviewed
        }

        review.setBook(book);
        review.setUser(user);
        reviewRepository.save(review);

        return "redirect:/reviews/book/" + bookId;
    }
}


