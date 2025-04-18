package com.bookshop.controller;

import com.bookshop.entity.Book;
import com.bookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookRepository bookRepository;

    // Show all books with optional search and sort
    @GetMapping
    public String listBooks(Model model,
                            @RequestParam(value = "query", required = false) String query,
                            @RequestParam(value = "sort", required = false) String sort,
                            @ModelAttribute("success") String successMessage) {

        List<Book> books;

        if (query != null && !query.isEmpty()) {
            books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublisherContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    query, query, query, query);
        } else if (sort != null) {
            switch (sort) {
                case "title_desc" -> books = bookRepository.findAllByOrderByTitleDesc();
                case "price" -> books = bookRepository.findAllByOrderByPriceAsc();
                case "price_desc" -> books = bookRepository.findAllByOrderByPriceDesc();
                case "stock" -> books = bookRepository.findAllByOrderByStockAsc();
                case "stock_desc" -> books = bookRepository.findAllByOrderByStockDesc();
                default -> books = bookRepository.findAllByOrderByTitleAsc(); // default A-Z
            }
        } else {
            books = bookRepository.findAllByOrderByTitleAsc(); // default if nothing is selected
        }

        model.addAttribute("books", books);
        model.addAttribute("query", query);
        model.addAttribute("sort", sort);
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("success", successMessage);
        }

        return "admin-book-list";
    }

    @GetMapping("/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin-book-form";
    }

    @PostMapping("/new")
    public String saveNewBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("success", "Book added successfully.");
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
        model.addAttribute("book", book);
        return "admin-book-form";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        book.setId(id);
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("success", "Book updated successfully.");
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", "Book could not be deleted (it may be part of an order).");
        }
        return "redirect:/admin/books";
    }
}
