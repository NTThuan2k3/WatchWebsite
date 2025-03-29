package com.example.Watch.controller;

import com.example.Watch.model.Product;
import com.example.Watch.model.Review;
import com.example.Watch.model.User;
import com.example.Watch.service.ProductService;
import com.example.Watch.service.ReviewService;
import com.example.Watch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final UserService userService;

//    @GetMapping("/{id}")
//    public String listReviews(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("reviews", reviewService.getAllReviews(id));
//        return "review/list";
//    }

    @GetMapping("/{id}")
    public String listReviews(@PathVariable("id") Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "6") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewsPage = reviewService.getAllReviews(id, pageable);
        model.addAttribute("reviewsPage", reviewsPage);
        model.addAttribute("currentPage", reviewsPage.getNumber());
        model.addAttribute("totalPages", reviewsPage.getTotalPages());
        model.addAttribute("productId", id);
        return "review/list";
    }

    @GetMapping("/add/{productId}")
    public String addReviewForm(@PathVariable("productId") Long productId, Model model) {
        Review review = new Review();
        review.setProduct(new Product());
        review.getProduct().setId(productId);
        model.addAttribute("review", review);
        return "review/add";
    }

    @PostMapping("/add")
    public String addReview(@Valid @ModelAttribute("review") Review review, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "review/add";
        }

        Product product = productService.getProductById(review.getProduct().getId())
                .orElseThrow(() -> new IllegalStateException("Product not found with ID: " + review.getProduct().getId()));
        review.setProduct(product);

        reviewService.addReview(review);
        return "redirect:/reviews/" + product.getId();
    }

    @GetMapping("/edit/{id}")
    public String editReviewForm(@PathVariable("id") Long id, Model model) {
        Review review = reviewService.getReviewById(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid review ID:" + id));
        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            name = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            name = principal.toString();
        }
        User user = userService.findByUsername(name)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + name));
        if (review.getUser().equals(user)) {
            model.addAttribute("review", review);
            return "review/edit";
        }
        Long productId = review.getProduct().getId();
        return "redirect:/reviews/" + productId;
    }

    @PostMapping("/edit/{id}")
    public String updateReview(@PathVariable("id") Long id, @Valid @ModelAttribute("review") Review review, BindingResult result, Model model) {
        if (result.hasErrors()) {
            review.setId(id);
            return "review/edit";
        }
        reviewService.updateReview(review);
        Long productId = review.getProduct().getId();
        return "redirect:/reviews/" + productId;
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id, Model model) {
        Review review = reviewService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID:" + id));
        Long productId = review.getProduct().getId();
        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            name = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            name = principal.toString();
        }
        User user = userService.findByUsername(name)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + name));
        if (review.getUser().equals(user)) {
            reviewService.deleteReviewById(id);
        }

        return "redirect:/reviews/" + productId;
    }
}
