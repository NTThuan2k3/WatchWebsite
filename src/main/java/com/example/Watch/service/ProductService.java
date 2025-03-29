package com.example.Watch.service;

import com.example.Watch.model.Product;
import com.example.Watch.repositoty.ProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional

public class ProductService {
    private final ProductRepository productRepository;

    // Retrieve all products from the database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve a product by its id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Add a new product to the database
    public void addProduct(Product product, MultipartFile image) {
        try {
            if (!image.isEmpty()) {
                product.setProductImage(image.getBytes());
            }
            productRepository.save(product);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    // Update an existing product
    public Product updateProduct(@NotNull Product product, MultipartFile image) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " + product.getId() + " does not exist."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());

        try {
            if (image != null && !image.isEmpty()) {
                existingProduct.setProductImage(image.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store image", e);
        }

        return productRepository.save(existingProduct);
    }

    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}