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

    // Show all books
    @GetMapping
    public String listBooks(Model model, @ModelAttribute("success") String successMessage) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);

        if (!successMessage.isEmpty()) {
            model.addAttribute("success", successMessage);
        }

        return "admin-book-list";
    }

    // Show form to add a new book
    @GetMapping("/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin-book-form";
    }

    // Handle form submission for adding a new book
    @PostMapping("/new")
    public String saveNewBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("success", "Book added successfully.");
        return "redirect:/admin/books";
    }

    // Show form to edit a book
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
        model.addAttribute("book", book);
        return "admin-book-form";
    }

    // Handle form submission for editing a book
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        book.setId(id);
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("success", "Book updated successfully.");
        return "redirect:/admin/books";
    }

    // Delete a book
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
