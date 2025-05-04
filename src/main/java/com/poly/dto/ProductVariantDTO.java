package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    private Integer productId;
    private String productName;
    private String productImage;
    private String productDescription;
    private Boolean productStatus;
    private Integer productQuantity;
    private String categoryName;
    private String brandName;

    private Integer variantId;
    private Integer variantQuantity;
    private Double variantPrice;
    private String variantImage;
    private String colorName;
    private String sizeName;
}
