package com.poly.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryDTO {
    private String productName;
    private int quantityAvailable;
    private double price;
    private String colorName;
    private String sizeName;
    private String productImage;   // Thêm hình ảnh sản phẩm
    private String variantImage;   // Thêm hình ảnh biến thể
}
