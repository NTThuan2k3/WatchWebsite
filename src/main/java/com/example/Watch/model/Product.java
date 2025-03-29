package com.example.Watch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private double price;

    private String brand;

    private String description;

    private String size;

    private String color;

    private int quantity;

    private boolean isHot;

    private String gender;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Lob
    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;
}
