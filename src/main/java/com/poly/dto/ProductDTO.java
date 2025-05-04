package com.poly.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    private String nameProd;
    private String description;
    private String imageProd;
    private boolean statusProd;
    private int sumQuantity;
    private CategoryDTO category;
    private BrandDTO brand;
    private List<VariantProductDTO> variants;
}
