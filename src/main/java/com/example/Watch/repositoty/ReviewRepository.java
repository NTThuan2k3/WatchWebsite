package com.example.Watch.repositoty;

import com.example.Watch.model.OrderDetail;
import com.example.Watch.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //List<Review> findByProductId(Long productId);
    Page<Review> findByProductId(Long productId, Pageable pageable);
}
