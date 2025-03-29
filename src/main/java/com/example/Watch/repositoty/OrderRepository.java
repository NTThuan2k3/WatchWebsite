package com.example.Watch.repositoty;

import com.example.Watch.model.Order;
import com.example.Watch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUser(User user);
}
