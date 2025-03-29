package com.example.Watch.service;

import com.example.Watch.model.OrderDetail;
import com.example.Watch.repositoty.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getAllOrderDetailsByOrderId(Long id) {
        return orderDetailRepository.findByOrderId(id);
    }
}
