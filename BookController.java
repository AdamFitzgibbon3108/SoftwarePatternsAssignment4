package com.bookshop.controller;

import com.bookshop.entity.Book;
import com.bookshop.repository.BookRepository;
import com.bookshop.sorting.SortStrategy;
import com.bookshop.sorting.SortStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public String listBooks(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            Model model) {

        // Step 1: Search
        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublisherContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    search, search, search, search
            );
        } else {
            books = bookRepository.findAll();
        }

        // Step 2: Apply Sorting (Strategy Pattern)
        SortStrategy strategy = SortStrategyFactory.getStrategy(sortBy);
        if (strategy != null) {
            books = strategy.sort(books);
        }

        // Step 3: Pass data to view
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);

        return "book-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping
    public String saveBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow();
        model.addAttribute("book", book);
        return "book-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    // Redirect /books/{id} to the review controller for full details
    @GetMapping("/{id}")
    public String redirectToBookWithReviews(@PathVariable Long id) {
        return "redirect:/reviews/book/" + id;
    }
}

