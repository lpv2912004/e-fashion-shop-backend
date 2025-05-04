package com.poly.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantProductDTO {
    private Integer id;
    private int quantity;
    private double price;
    private String imageVariant;
    private int status_VP;
    private ColorDTO color;
    private SizeDTO size;
//    private FlashSaleDTO flashSale;

}
