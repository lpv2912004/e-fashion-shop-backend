package com.poly.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaleDTO {
    private int productId;
    private String productName;
    private double originalPrice;
    private double discountedPrice;
    private int discountPercent;
    private String imageUrl;

    // Constructors, Getters and Setters
}

