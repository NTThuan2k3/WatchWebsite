package com.example.Watch.service;

import com.example.Watch.model.Product;
import com.example.Watch.model.Review;
import com.example.Watch.model.User;
import com.example.Watch.repositoty.ProductRepository;
import com.example.Watch.repositoty.ReviewRepository;
import com.example.Watch.repositoty.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

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

//    public List<Review> getAllReviews(Long id) {
//        return reviewRepository.findByProductId(id);
//    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public void addReview(Review review) {
        review.setCommentDate(LocalDateTime.now());

        String name = getCurrentUsername();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + name));
        review.setUser(user);

        reviewRepository.save(review);
    }

    public void updateReview(@NotNull Review review) {
        Review existingReview = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new IllegalStateException("Review with ID " + review.getId() + " does not exist."));
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        reviewRepository.save(existingReview);
    }

    public void deleteReviewById(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalStateException("Review with ID " + id + " does not exist.");
        }
        reviewRepository.deleteById(id);
    }

    public Page<Review> getAllReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }
}
