package com.bookshop.cart;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Component
@SessionScope
public class ShoppingCart {

    private Map<Long, CartItem> items = new HashMap<>(); // key = book ID

    public void addItem(Long bookId, CartItem item) {
        if (items.containsKey(bookId)) {
            CartItem existing = items.get(bookId);
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            items.put(bookId, item);
        }
    }

    public void removeItem(Long bookId) {
        items.remove(bookId);
    }

    public void clear() {
        items.clear();
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public double getTotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
