package com.example.Watch.controller;

import com.example.Watch.model.OrderDetail;
import com.example.Watch.service.OrderDetailService;
import com.example.Watch.service.OrderService;
import com.example.Watch.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orderDetails")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping("/{id}")
    public String listOrderDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("orderDetails", orderDetailService.getAllOrderDetailsByOrderId(id));
        return "/orderDetails/list";
    }
}

