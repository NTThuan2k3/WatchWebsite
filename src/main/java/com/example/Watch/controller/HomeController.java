package com.example.Watch.controller;

import com.example.Watch.model.Product;
import com.example.Watch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String home(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProducts(pageable);
        model.addAttribute("productPage", productPage);
        return "home/index";
    }
}