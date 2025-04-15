package com.bookshop.controller;

import com.bookshop.entity.Book;
import com.bookshop.entity.User;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Book> books = bookRepository.findAll();
        List<User> users = userRepository.findAll();

        model.addAttribute("books", books);
        model.addAttribute("users", users);
        model.addAttribute("bookCount", books.size());
        model.addAttribute("userCount", users.size());

        return "admin-dashboard";
    }
}

