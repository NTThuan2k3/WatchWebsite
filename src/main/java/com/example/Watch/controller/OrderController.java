package com.example.Watch.controller;

import com.example.Watch.model.CartItem;
import com.example.Watch.model.Order;
import com.example.Watch.model.OrderDetail;
import com.example.Watch.service.CartService;
import com.example.Watch.service.OrderDetailService;
import com.example.Watch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(String customerName, String address, String phone, String email, String notes, String paymentMethods, RedirectAttributes redirectAttributes, Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }

        redirectAttributes.addFlashAttribute("totalPrice", cartService.calculateTotalPrice());

        orderService.createOrder(customerName, address, phone, email, notes, paymentMethods, cartItems);

        redirectAttributes.addFlashAttribute("customerName", customerName);
        redirectAttributes.addFlashAttribute("address", address);
        redirectAttributes.addFlashAttribute("phone", phone);
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("notes", notes);
        redirectAttributes.addFlashAttribute("paymentMethods", paymentMethods);
        redirectAttributes.addFlashAttribute("message", "Đơn hàng của bạn đã được đặt thành công.");

        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }

    @GetMapping
    public String listOrder(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "/order/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:"
                        + id));
        orderService.deleteOrderById(id);
        model.addAttribute("orders", orderService.getOrderById(id));
        return "redirect:/order";
    }
}