package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {
    private String productName;
    private Integer quantity;
    private Double price;
    private String colorName;
    private String sizeName;
}
