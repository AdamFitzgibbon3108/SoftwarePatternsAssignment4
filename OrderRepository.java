package com.bookshop.repository;

import com.bookshop.entity.Order;
import com.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double sumAllOrderTotals();

    
}
