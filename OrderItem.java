package com.bookshop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the book
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int quantity;
    private double price; // Price at the time of purchase

    // Link back to the order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Constructors
    public OrderItem() {}

    public OrderItem(Book book, int quantity, double price, Order order) {
        this.book = book;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
