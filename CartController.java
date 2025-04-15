package com.bookshop.controller;

import com.bookshop.cart.CartItem;
import com.bookshop.cart.ShoppingCart;
import com.bookshop.entity.*;
import com.bookshop.repository.*;
import com.bookshop.service.DiscountService;

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

    @Autowired
    private DiscountService discountService;

    // View cart
    @GetMapping
    public String viewCart(Model model, @ModelAttribute("error") String error, @ModelAttribute("message") String message) {
        model.addAttribute("items", shoppingCart.getItems());
        model.addAttribute("total", shoppingCart.getTotal());
        if (!error.isEmpty()) model.addAttribute("error", error);
        if (!message.isEmpty()) model.addAttribute("message", message);
        return "cart";
    }

    // Add to cart
    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, @RequestParam(defaultValue = "1") int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        CartItem item = new CartItem(book, quantity);
        shoppingCart.addItem(bookId, item);
        return "redirect:/cart";
    }

    // Remove from cart
    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId) {
        shoppingCart.removeItem(bookId);
        return "redirect:/cart";
    }

    // Checkout
    @PostMapping("/checkout")
    public String checkout(RedirectAttributes redirectAttributes, Principal principal) {
        if (shoppingCart.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Your cart is empty.");
            return "redirect:/cart";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        double total = 0;

        for (CartItem cartItem : shoppingCart.getItems()) {
            Book book = cartItem.getBook();

            if (book.getStock() < cartItem.getQuantity()) {
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

            total += book.getPrice() * cartItem.getQuantity();
        }

        double discountRate = discountService.calculateDiscount(user, total);
        double discountAmount = total * discountRate;
        double finalTotal = total - discountAmount;

        List<String> discountReasons = discountService.getDiscountReasons(user, total);

        order.setItems(orderItems);
        order.setTotalAmount(finalTotal);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        shoppingCart.clear();

        redirectAttributes.addFlashAttribute("order", order);
        redirectAttributes.addFlashAttribute("originalTotal", total);
        redirectAttributes.addFlashAttribute("discountAmount", discountAmount);
        redirectAttributes.addFlashAttribute("discountApplied", discountRate > 0);
        redirectAttributes.addFlashAttribute("discountReasons", discountReasons);

        return "redirect:/cart/success";
    }

    @GetMapping("/success")
    public String checkoutSuccess(@ModelAttribute("order") Order order,
                                  @ModelAttribute("originalTotal") Double originalTotal,
                                  @ModelAttribute("discountAmount") Double discountAmount,
                                  @ModelAttribute("discountApplied") Boolean discountApplied,
                                  @ModelAttribute("discountReasons") List<String> discountReasons,
                                  Model model) {

        model.addAttribute("order", order);
        model.addAttribute("originalTotal", originalTotal);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("discountApplied", discountApplied);
        model.addAttribute("discountReasons", discountReasons);

        return "checkout-success";
    }
}

