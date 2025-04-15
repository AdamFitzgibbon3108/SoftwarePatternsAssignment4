package com.bookshop.service;

import com.bookshop.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountService {

    public double calculateDiscount(User user, double currentOrderAmount) {
        double totalSpent = user.getTotalSpent();
        String cardType = user.getCardType();

        double discount = 0.0;

        // Enhanced discount tiers based on total spent
        if (totalSpent >= 1000) {
            discount += 0.30; // 30% off
        } else if (totalSpent >= 500) {
            discount += 0.20; // 20% off
        } else if (totalSpent >= 200) {
            discount += 0.15; // 15% off
        }

        // Extra for using VISA
        if ("VISA".equalsIgnoreCase(cardType)) {
            discount += 0.10; // Additional 10%
        }

        // Extra for big order
        if (currentOrderAmount >= 150) {
            discount += 0.10; // Additional 10%
        }

        return Math.min(discount, 0.60); // Cap at 60%
    }

    public List<String> getDiscountReasons(User user, double currentOrderAmount) {
        double totalSpent = user.getTotalSpent();
        String cardType = user.getCardType();

        List<String> reasons = new ArrayList<>();

        if (totalSpent >= 1000) {
            reasons.add("Spent over $1000 on past orders (30%)");
        } else if (totalSpent >= 500) {
            reasons.add("Spent over $500 on past orders (20%)");
        } else if (totalSpent >= 200) {
            reasons.add("Spent over $200 on past orders (15%)");
        }

        if ("VISA".equalsIgnoreCase(cardType)) {
            reasons.add("Paid with VISA card (10%)");
        }

        if (currentOrderAmount >= 150) {
            reasons.add("Large order over $150 (10%)");
        }

        return reasons;
    }
}
