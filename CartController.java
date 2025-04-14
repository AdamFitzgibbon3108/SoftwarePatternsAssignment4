package com.bookshop.controller;

import com.bookshop.cart.CartItem;
import com.bookshop.cart.ShoppingCart;
import com.bookshop.entity.*;
import com.bookshop.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ShoppingCart shoppingCart;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    // View cart
    @GetMapping
    public String viewCart(Model model, @ModelAttribute("error") String error, @ModelAttribute("message") String message) {
        System.out.println(">> Viewing cart. Items in cart: " + shoppingCart.getItems().size());
        model.addAttribute("items", shoppingCart.getItems());
        model.addAttribute("total", shoppingCart.getTotal());
        if (!error.isEmpty()) model.addAttribute("error", error);
        if (!message.isEmpty()) model.addAttribute("message", message);
        return "cart";
    }

    // Add book to cart
    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, @RequestParam(defaultValue = "1") int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book ID"));
        CartItem item = new CartItem(book, quantity);
        shoppingCart.addItem(bookId, item);
        System.out.println(">> Book added to cart: " + book.getTitle() + " x" + quantity);
        return "redirect:/cart";
    }

    // Remove book from cart
    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId) {
        shoppingCart.removeItem(bookId);
        System.out.println(">> Book removed from cart. ID: " + bookId);
        return "redirect:/cart";
    }

    // Checkout and save order
    @PostMapping("/checkout")
    public String checkout(RedirectAttributes redirectAttributes, Principal principal) {
        System.out.println(">> Attempting checkout for user: " + principal.getName());
        System.out.println(">> Cart size: " + shoppingCart.getItems().size());

        if (shoppingCart.isEmpty()) {
            System.out.println(">> Checkout aborted: Cart is empty.");
            redirectAttributes.addFlashAttribute("message", "Your cart is empty.");
            return "redirect:/cart";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        System.out.println(">> Found user: " + user.getUsername());

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(shoppingCart.getTotal());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : shoppingCart.getItems()) {
            Book book = cartItem.getBook();

            System.out.println(">> Processing item: " + book.getTitle() + " (Qty: " + cartItem.getQuantity() + ")");

            if (book.getStock() < cartItem.getQuantity()) {
                System.out.println(">> Not enough stock for: " + book.getTitle());
                redirectAttributes.addFlashAttribute("error", "Not enough stock for: " + book.getTitle());
                return "redirect:/cart";
            }

            book.setStock(book.getStock() - cartItem.getQuantity());
            bookRepository.save(book);

            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        shoppingCart.clear();
        System.out.println(">> Checkout completed. Order saved and cart cleared.");

        redirectAttributes.addFlashAttribute("order", order);
        return "redirect:/cart/success";
    }

    // Show success page
    @GetMapping("/success")
    public String checkoutSuccess(@ModelAttribute("order") Order order, Model model) {
        System.out.println(">> Displaying order success page for Order ID: " + (order != null ? order.getId() : "null"));
        model.addAttribute("order", order);
        return "checkout-success";
    }
}

