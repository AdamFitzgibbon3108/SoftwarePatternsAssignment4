# System Architecture & Database Report

## System Overview

The Online Bookshop is built using a layered architecture:

- **Presentation Layer**: Thymeleaf templates
- **Controller Layer**: Handles web requests
- **Service Layer**: (Optional in future work)
- **Repository Layer**: Interfaces to interact with DB
- **Data Layer**: JPA Entities map to tables

---

## Database Schema

### Tables:

1. **users**
   - `id`, `username`, `password`, `email`, `role`, `full_name`, `shipping_address`, `card_number`, `card_type`, `total_spent`, `created_at`

2. **book**
   - `id`, `title`, `author`, `publisher`, `price`, `category`, `isbn`, `image_url`, `stock`

3. **orders**
   - `id`, `user_id` (FK), `total_amount`, `order_date`

4. **order_items**
   - `id`, `order_id` (FK), `book_id` (FK), `quantity`, `price`

5. **review**
   - `id`, `user_id` (FK), `book_id` (FK), `rating`, `comment`, `created_at`

---

## Use Cases

### 1. Customer Registration
- Fill form with shipping/payment details
- Data saved in `users` table with role = CUSTOMER

### 2. Customer Book Browsing
- Search, filter, sort by title/price/category
- Books pulled from `book` table

### 3. Cart and Checkout
- Add items to cart
- On checkout: create `order`, store `order_items`, update `stock`, update `total_spent`

### 4. View Orders (Customer)
- User accesses order history
- Orders fetched by user ID

### 5. Admin Dashboard
- Access all data: `book.count()`, `user.count()`, `order.count()`, `SUM(total_amount)`

### 6. Manage Books
- Admin can create, update, or delete books
- Deleting a book will also remove related orders

### 7. Manage Customers
- Admin views all customers (Iterator Pattern)
- Can click to view individual customer order history

---

## Design Requirements

- Secure login system with role-based access (`ADMIN`, `CUSTOMER`)
- Admin-only access to dashboards and book management
- Use of GoF patterns (at least 2) — achieved via Iterator and Strategy
- Extendable and clean codebase using Spring Boot best practices

---

## Summary

This bookshop system delivers a full set of e-commerce functionalities while demonstrating strong application of software design principles and patterns. It is modular, maintainable, and ready for future enhancements like promotions, analytics, and REST API exposure.
