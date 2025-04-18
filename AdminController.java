package com.bookshop.controller;

import com.bookshop.entity.Order;
import com.bookshop.entity.User;
import com.bookshop.iterator.CustomerCollection;
import com.bookshop.iterator.CustomerIterator;
import com.bookshop.iterator.CustomerList;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.OrderRepository;
import com.bookshop.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long totalBooks = bookRepository.count();
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();
        Double totalRevenue = orderRepository.sumAllOrderTotals();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        CustomerCollection customerList = new CustomerList();
        for (User user : userRepository.findAll()) {
            System.out.println(">> User role: " + user.getRole()); // DEBUG
            if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
                customerList.addCustomer(user);
            }
        }

        CustomerIterator iterator = customerList.createIterator();
        List<User> customers = new ArrayList<>();
        while (iterator.hasNext()) {
            customers.add(iterator.next());
        }

        model.addAttribute("customers", customers);
        return "manage-customers";
    }


    @GetMapping("/users/{id}/orders")
    public String viewCustomerOrders(@PathVariable Long id,
                                     @RequestParam(value = "sort", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "dir", required = false, defaultValue = "asc") String sortDir,
                                     Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID: " + id));

        List<Order> orders = orderRepository.findByUser(user);

        Comparator<Order> comparator = switch (sortField) {
            case "amount" -> Comparator.comparing(Order::getTotalAmount);
            case "date" -> Comparator.comparing(Order::getOrderDate);
            default -> Comparator.comparing(Order::getId);
        };

        if ("desc".equals(sortDir)) {
            comparator = comparator.reversed();
        }

        orders.sort(comparator);

        model.addAttribute("customer", user);
        model.addAttribute("orders", orders);
        model.addAttribute("sort", sortField);
        model.addAttribute("dir", sortDir);
        model.addAttribute("reverseDir", sortDir.equals("asc") ? "desc" : "asc");

        return "view-customer-orders";
    }
}