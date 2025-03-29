package com.example.Watch.controller;

import com.example.Watch.model.Product;
import com.example.Watch.service.CategoryService;
import com.example.Watch.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // Display a list of all products
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/product-list";
    }

    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/add-product";
    }

    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Product product, BindingResult result,
                             @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }

        productService.addProduct(product, image);
        redirectAttributes.addFlashAttribute("message", "Product added successfully");
        return "redirect:/products";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "/products/details";
    }

    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }

    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product,
                                BindingResult result, @RequestParam("image") MultipartFile image,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "/products/update-product";}

        productService.updateProduct(product, image);
        redirectAttributes.addFlashAttribute("message", "Product updated successfully");
        return "redirect:/products";
    }

    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    // Endpoint to get the product image
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            byte[] image = product.getProductImage();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}