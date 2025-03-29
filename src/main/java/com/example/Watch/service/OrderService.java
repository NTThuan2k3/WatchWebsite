package com.example.Watch.service;

import com.example.Watch.model.*;
import com.example.Watch.repositoty.OrderDetailRepository;
import com.example.Watch.repositoty.OrderRepository;
import com.example.Watch.repositoty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Order createOrder(String customerName, String address, String phone, String email, String notes, String paymentMethods, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhone(phone);
        order.setEmail(email);
        order.setNotes(notes);
        order.setPaymentMethods(paymentMethods);

        // Đặt orderDate là ngày hiện hành
        order.setOrderDate(LocalDateTime.now());

        String name = getCurrentUsername();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + name));
        order.setUser(user);

        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
        cartService.clearCart();
        return order;
    }

    public List<Order> getAllOrders() {
        String name = getCurrentUsername();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + name));
        return orderRepository.findOrderByUser(user);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrderById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalStateException("Order with ID " + id + " does not exist.");
        }
        List<OrderDetail> detail = orderDetailRepository.findByOrderId(id);
        for (OrderDetail orderDetail : detail) {
            orderDetailRepository.deleteById(orderDetail.getId());
        }
        orderRepository.deleteById(id);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    //Hàm lấy user hiện tại
    private String getCurrentUsername() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}