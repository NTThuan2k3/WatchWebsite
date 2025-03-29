package com.example.Watch.controller;

import com.example.Watch.model.CartItem;
import com.example.Watch.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice());
        return "/cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") List<Long> productIds, @RequestParam("quantity") List<Integer> quantities) {
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int quantity = quantities.get(i);
            cartService.addToCart(productId, quantity);
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }
}