package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashSaleDTO3 {
    private Integer id;
    private String name_FS;
    private Boolean status;
    private Date created_date;
    private List<VariantDiscountDTO> variants;
}
