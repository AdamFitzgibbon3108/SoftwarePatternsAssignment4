package com.bookshop.controller;

import com.bookshop.entity.Order;
import com.bookshop.entity.User;
import com.bookshop.repository.OrderRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscountService discountService;

    @GetMapping("/history")
    public String showOrderHistory(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Order> orders = orderRepository.findByUser(user);

        // Sort by most recent
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());

        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders", orders.size());
        return "order-history";
    }
}
